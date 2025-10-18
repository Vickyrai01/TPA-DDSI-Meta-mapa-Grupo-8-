package cargadorProxy.model;

import cargadorProxy.repository.RepositoryFuentes;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CargadorProxy {
    private static CargadorProxy instance;
    private RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance() ;
    private volatile Duration umbralProcesamiento = Duration.ofSeconds(30);

    public void setUmbral(Duration d) { this.umbralProcesamiento = d; }

    public static CargadorProxy getInstance() {
        if (instance == null) {
            synchronized (CargadorProxy.class) {
                if (instance == null) {
                    instance = new CargadorProxy();
                }
            }
        }
        return instance;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        List<Fuente> fuentes = fuentesAProcesar();
        if(fuentes.isEmpty()) return List.of();

        List<HechoAIntegrarDTO> hechos = new ArrayList<>();
        for (Fuente f : fuentes) {
            try{
                List<HechoAIntegrarDTO> lote = f.extraerHechos();
                if (lote != null) {
                    lote.forEach(h -> h.setLinkFuente(f.getLink()));
                    hechos.addAll(lote);
                }
                f.setUltimoProcesamiento(Instant.now());
            } catch (Exception e) { System.err.println("Error procesando fuente " + f.getId() + ": " + e.getMessage());}

        }
        hechos.forEach(h -> h.setTipoFuente("PROXY"));
        return hechos;
    }

    public List<Fuente> fuentesAProcesar() {
        Instant corte = Instant.now().minus(umbralProcesamiento);
        return repositoryFuentes.findAll().stream()
                .filter(f -> f.getUltimoProcesamiento() == null || f.getUltimoProcesamiento().isBefore(corte))
                .toList();
    }

}
