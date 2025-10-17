package cargadorEstatica.model;
import cargadorEstatica.repository.RepositoryFuentes;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico {

    private static CargadorEstatico instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;
    private volatile Duration umbralProcesamiento = Duration.ofSeconds(30);

    public void setUmbral(Duration d) { this.umbralProcesamiento = d; }


    public static CargadorEstatico getInstance() {
        if (instance == null) {
            synchronized (CargadorEstatico.class) {
                if (instance == null) {
                    instance = new CargadorEstatico();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<Fuente> fuentes = fuentesAProcesar();

        if (fuentes.isEmpty()) return List.of();

        List<HechoAIntegrarDTO> hechos = new ArrayList<>();
        for (Fuente f : fuentes) {
            try {
                List<HechoAIntegrarDTO> lote = f.extraerHechos();
                if (lote != null) hechos.addAll(lote);
                f.setUltimoProcesamiento(Instant.now());
            } catch (Exception e) {
                System.err.println("Error procesando fuente: " + f.getId() + ":" + e.getMessage());}
            }
        hechos.forEach(h -> h.setTipoFuente("ESTATICA"));
        return hechos;
        }




    //Devuelve las fuentes que debo procesar: nunca procesadas o más viejas que umbral
    public List<Fuente> fuentesAProcesar() {
        Instant corte = Instant.now().minus(umbralProcesamiento);
        return repositoryFuentes.findAll().stream()
                .filter(f -> f.getUltimoProcesamiento() == null || f.getUltimoProcesamiento().isBefore(corte))
                .toList();
    }
}

