package models.entities.normalizador;

import models.repository.ColeccionesRepository;
import models.services.ServicioDeAgregacion;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;


    public final class ComparadorHechos {

        private static volatile ComparadorHechos instance;

        private ComparadorHechos() {
            if (instance != null) {
                throw new RuntimeException("Usa getInstance() para obtener el Singleton");
            }
        }

        public static ComparadorHechos getInstance() {
            if (instance == null) {
                synchronized (ServicioDeAgregacion.class) {
                    if (instance == null){
                        instance = new ComparadorHechos();
                    }
                }
            }
            return instance;
        }

        private double threshold = 0.75;
        public double getThreshold() {
            return threshold;
        }
        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public static class SimilarityResult {
            public final double score;
            public final boolean similar;
            public final Map<String, Double> componentScores;

            SimilarityResult(double score, boolean similar, Map<String, Double> parts) {
                this.score = score;
                this.similar = similar;
                this.componentScores = parts;
            }

            @Override
            public String toString() {
                return String.format(Locale.ROOT, "score=%.3f similar=%s parts=%s",
                        score, similar, componentScores);
            }
        }

        public static class Config {
            // Pesos por campo (pueden no sumar 1)
            public double wTitulo = 0.15;
            public double wDescripcion = 0.15;
            public double wCategoria = 0.05;
            public double wUbicacion = 0.45;
            public double wFecha = 0.20;

            public double threshold = 0.70;

            //Máxima distancia para similitud de ubicación
            public double maxMeters = 500.0;

            //Máxima diferencia de días para similitud de fecha
            public int maxDays = 7;

            public Set<String> stopwords = new HashSet<>(Arrays.asList(
                    "el", "la", "los", "las", "de", "del", "y", "o", "u", "en", "a", "un", "una",
                    "para", "por", "con", "que", "se", "su", "sus", "al", "lo", "es", "son",
                    "esta", "este", "esta", "estas", "estos", "un", "una"
            ));

            //Parser de fecha: convierte String -> LocalDate (null si no se puede).
            public Function<String, LocalDate> dateParser = s -> {
                try { // default: ISO yyyy-MM-dd
                    return s == null || s.trim().isEmpty() ? null
                            : LocalDate.parse(s.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    return null;
                }
            };
        }

        public static SimilarityResult compare(HechoAIntegrarDTO a, HechoAIntegrarDTO b, Config cfg) {
            Objects.requireNonNull(a);
            Objects.requireNonNull(b);
            Objects.requireNonNull(cfg);
            Map<String, Double> parts = new LinkedHashMap<>();
            List<Double> weights = new ArrayList<>();
            List<Double> scores = new ArrayList<>();
            List<String> names = new ArrayList<>();

            Double sTitulo = textJaccard(a.getTitulo(), b.getTitulo(), cfg);
            if (sTitulo != null) {
                add("titulo", sTitulo, cfg.wTitulo, parts, names, scores, weights);
            }

            // Descripción (Jaccard)
            Double sDesc = textJaccard(a.getDescripcion(), b.getDescripcion(), cfg);
            if (sDesc != null) {
                add("descripcion", sDesc, cfg.wDescripcion, parts, names, scores, weights);
            }

            // Categoría (igual o Jaccard tokens si difiere)
            Double sCat = categorySim(a.getCategoria(), b.getCategoria(), cfg);
            if (sCat != null) {
                add("categoria", sCat, cfg.wCategoria, parts, names, scores, weights);
            }

            // Ubicación (Haversine → similitud lineal)
            Double sGeo = geoSim(a.getLatitud(), a.getLongitud(), b.getLatitud(), b.getLongitud(), cfg.maxMeters);
            if (sGeo != null) {
                add("ubicacion", sGeo, cfg.wUbicacion, parts, names, scores, weights);
            }

            // Fecha (días → similitud lineal)
            Double sFecha = dateSim(a.getFechaDeHecho(), b.getFechaDeHecho(), cfg);
            if (sFecha != null) {
                add("fecha", sFecha, cfg.wFecha, parts, names, scores, weights);
            }

            // Si nada aportó, devolvemos 0
            double totalWeight = weights.stream().mapToDouble(Double::doubleValue).sum();
            if (totalWeight == 0.0) return new SimilarityResult(0.0, false, parts);

            // Score final (re-normalizando pesos presentes)
            double weighted = 0.0;
            for (int i = 0; i < scores.size(); i++) {
                weighted += (weights.get(i) / totalWeight) * scores.get(i);
            }
            boolean similar = weighted >= cfg.threshold;
            return new SimilarityResult(weighted, similar, parts);
        }

        // ---------- helpers de componentes ----------

        private static void add(String name, double score, double weight,
                                Map<String, Double> parts, List<String> names,
                                List<Double> scores, List<Double> weights) {
            if (weight <= 0) return; // peso deshabilitado
            parts.put(name, clamp01(score));
            names.add(name);
            scores.add(clamp01(score));
            weights.add(weight);
        }

        /**
         * Similitud de texto por Jaccard de tokens (0..1); null si ambos vacíos o nulos.
         */
        private static Double textJaccard(String t1, String t2, Config cfg) {
            Set<String> a = tokenize(t1, cfg.stopwords);
            Set<String> b = tokenize(t2, cfg.stopwords);
            if (a.isEmpty() && b.isEmpty()) return null;
            if (a.isEmpty() || b.isEmpty()) return 0.0;
            int inter = 0;
            for (String s : a) if (b.contains(s)) inter++;
            int union = a.size() + b.size() - inter;
            return union == 0 ? 0.0 : (double) inter / union;
        }

        /**
         * Similitud de categoría: 1.0 si iguales (case-insensitive y normalizadas), sino Jaccard tokens.
         */
        private static Double categorySim(String c1, String c2, Config cfg) {
            if (isBlank(c1) && isBlank(c2)) return null;
            if (isBlank(c1) || isBlank(c2)) return 0.0;
            String n1 = normalize(c1);
            String n2 = normalize(c2);
            if (n1.equals(n2)) return 1.0;
            return textJaccard(c1, c2, cfg);
        }

        /**
         * Similitud geográfica: 1 a 0 según distancia y radio; null si falta alguna coord o maxMeters<=0.
         */
        private static Double geoSim(String lat1, String lon1, String lat2, String lon2, double maxMeters) {
            if (maxMeters <= 0) return null;
            Double la1 = parseDouble(lat1), lo1 = parseDouble(lon1);
            Double la2 = parseDouble(lat2), lo2 = parseDouble(lon2);
            if (la1 == null || lo1 == null || la2 == null || lo2 == null) return null;
            double d = haversineMeters(la1, lo1, la2, lo2);
            if (d >= maxMeters) return 0.0;
            return 1.0 - (d / maxMeters); // lineal
        }

        /**
         * Similitud de fecha: 1 si mismo día, decae lineal a 0 en maxDays; null si falta alguna o maxDays<=0 o parser falla.
         */
        private static Double dateSim(String f1, String f2, Config cfg) {
            if (cfg.maxDays <= 0) return null;
            LocalDate d1 = cfg.dateParser.apply(f1);
            LocalDate d2 = cfg.dateParser.apply(f2);
            if (d1 == null || d2 == null) return null;
            long dd = Math.abs(java.time.temporal.ChronoUnit.DAYS.between(d1, d2));
            if (dd >= cfg.maxDays) return 0.0;
            return 1.0 - ((double) dd / cfg.maxDays);
        }

        // ---------- utilitarios ----------

        private static Set<String> tokenize(String text, Set<String> stop) {
            Set<String> out = new HashSet<>();
            if (isBlank(text)) return out;
            String n = normalize(text);
            for (String tok : n.split("\\s+")) {
                if (tok.length() < 2) continue;
                if (stop.contains(tok)) continue;
                out.add(tok);
            }
            return out;
        }

        /**
         * Normaliza: lower, quita acentos, quita signos y colapsa espacios.
         */
        private static String normalize(String s) {
            if (s == null) return "";
            String lower = s.toLowerCase(Locale.ROOT);
            String deacc = Normalizer.normalize(lower, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}+", "");
            String lettersDigits = deacc.replaceAll("[^\\p{L}\\p{Nd}]+", " ");
            return lettersDigits.trim().replaceAll("\\s+", " ");
        }

        private static boolean isBlank(String s) {
            return s == null || s.trim().isEmpty();
        }

        private static Double parseDouble(String s) {
            if (isBlank(s)) return null;
            try {
                return Double.parseDouble(s.trim());
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Distancia Haversine en metros.
         */
        private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
            double R = 6371000.0; // m
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return R * c;
        }

        private static double clamp01(double v) {
            return Math.max(0.0, Math.min(1.0, v));
        }

        /*
        // --------- Demo mínima ---------
        public static void main(String[] args) {
            Hecho h1 = new Hecho(
                    "Robo a mano armada en Palermo",
                    "Sustrajeron una moto en av. Santa Fe y Scalabrini Ortiz.",
                    "Seguridad",
                    "-26.8528", "-65.7090",
                    "2025-08-15"
            );
            Hecho h2 = new Hecho(
                    "Robo de moto en Palermo",
                    "En Santa Fe y Scalabrini Ortiz reportan sustracción de una motocicleta.",
                    "seguridad",
                    "-26.8528", "-65.7098",
                    "2025-08-16"
            );
            Hecho h3=new Hecho(
                    "Robo de verduleria en Palermo",
                    "Sobre Santa Fe se afanaron un cajon de tomates.",
                    "seguridad",
                    "-34.581", "-58.412",
                    "2025-08-16"
            );

            Config cfg = new Config();
            cfg.threshold = 0.70;   // más alto = más estricta
            cfg.maxMeters = 300.0;  // 100 m de radio para que ubicación aporte 1→0
            cfg.maxDays = 2;        // 3 días de ventana para fecha
            // Si tenés tu parser flexible:
            // cfg.dateParser = s -> LocalDate.parse(FlexibleDateParser.toIsoOrReview(s));

            SimilarityResult r = compare(h1, h2, cfg);
            System.out.println(r);
        }
        */
        public Boolean esElMismoHecho(HechoAIntegrarDTO hecho1, HechoAIntegrarDTO hecho2) {
            Config cfg = new Config();
            cfg.threshold = getThreshold();   // más alto = más estricta
            cfg.maxMeters = 500.0;  // 500 m de radio para que ubicación aporte 1→0
            cfg.maxDays = 2;        // 2 días de ventana para fecha

            SimilarityResult r = compare(hecho1, hecho2, cfg);

            return r.similar;
        }

        public double similitudDeHechos(HechoAIntegrarDTO hecho1, HechoAIntegrarDTO hecho2){
            Config cfg = new Config();
            cfg.threshold = getThreshold();   // más alto = más estricta
            cfg.maxMeters = 500.0;  // 500 m de radio para que ubicación aporte 1→0
            cfg.maxDays = 2;        // 2 días de ventana para fecha

            SimilarityResult r = compare(hecho1, hecho2, cfg);

            return r.score;
        }
    }
