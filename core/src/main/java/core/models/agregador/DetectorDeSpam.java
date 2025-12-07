package core.models.agregador;

import core.models.entities.solicitud.SolicitudDeEliminacion;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetectorDeSpam {

    private static volatile DetectorDeSpam instance;

   // public static boolean esSpam(String solicitud) {return false;}

    public DetectorDeSpam() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }


    public static DetectorDeSpam getInstance() {
        if (instance == null) {
            synchronized (DetectorDeSpam.class) {
                if (instance == null) {
                    instance = new DetectorDeSpam();
                }
            }
        }
        return instance;
    }

    // Palabras típicas de spam con un peso "tipo IDF" (a mano)
    private static final Map<String, Double> PESOS_SPAM = new HashMap<>();

    // Umbral para decidir si es spam
    private static final double UMBRAL_SPAM = 3.0;

    static {
        // Publicidad
        PESOS_SPAM.put("oferta", 1.5);
        PESOS_SPAM.put("promocion", 1.7);
        PESOS_SPAM.put("promo", 1.6);

        PESOS_SPAM.put("suscribite", 2.0);
        PESOS_SPAM.put("seguinos", 1.8);
        PESOS_SPAM.put("click", 1.4);
        PESOS_SPAM.put("comprar", 1.7);
        PESOS_SPAM.put("venta", 1.4);

        // Estafas
        PESOS_SPAM.put("casino", 2.2);
        PESOS_SPAM.put("bono", 1.8);
        PESOS_SPAM.put("gratis", 1.6);
        PESOS_SPAM.put("cripto", 1.9);
        PESOS_SPAM.put("inverti", 1.9);

        // Palabras irrelevantes / bots
        PESOS_SPAM.put("asdf", 2.0);
        PESOS_SPAM.put("lorem", 1.7);
        PESOS_SPAM.put("ipsum", 1.7);
        PESOS_SPAM.put("prueba", 1.5);
        PESOS_SPAM.put("xxxx", 1.8);
        PESOS_SPAM.put("zzzz", 1.8);

        // URLs
        PESOS_SPAM.put("http", 2.5);
        PESOS_SPAM.put("https", 2.5);
        PESOS_SPAM.put("www", 2.5);
    }

    public  boolean esSpam(String texto) {
       if (contieneChocloSinEspacios(texto))
       {return true;}

        if (texto == null || texto.isBlank()) {
            // si viene vacío, para este ejemplo lo consideramos inválido / spam
            return true;
        }

        String normalizado = normalizar(texto);
        String[] palabras = normalizado.split("\\s+");

        // TF-IDF súper simplificado: sumo tf * peso
        double score = 0.0;

        for (String palabra : palabras) {
            Double peso = PESOS_SPAM.get(palabra);
            if (peso != null) {
                // cada aparición suma TF * IDF (acá TF = 1 por aparición)
                score += peso;
            }
        }

        // si el texto es muy cortito, también sospechoso (no cumple 500 chars)
        if (texto.trim().length() < 500) {
            score += 2.0; // le subimos el score de sospecha
        }

        return score >= UMBRAL_SPAM;
    }

    // Normaliza: minúsculas, sin acentos, solo letras/espacios
    private String normalizar(String texto) {
        String t = texto.toLowerCase(Locale.ROOT);

        t = Normalizer.normalize(t, Normalizer.Form.NFD);
        t = t.replaceAll("\\p{M}", ""); // elimina acentos

        // dejar solo letras, números básicos y espacios
        t = t.replaceAll("[^a-z0-9ñáéíóúü ]", " ");

        return t;
    }

    private boolean contieneChocloSinEspacios(String texto) {
        if (texto == null) return false;

        // Quitamos saltos de línea por si vienen pegados
        String limpio = texto.replace("\n", " ").replace("\r", " ").trim();

        // Umbral razonable: una palabra de más de 50 caracteres ya es sospechosa
        int UMBRAL_CHOCLO = 50;

        String[] palabras = limpio.split("\\s+");

        for (String p : palabras) {
            if (p.length() > UMBRAL_CHOCLO) {
                return true; // Encontramos spam tipo “choclo”
            }
        }

        return false;
    }
}
