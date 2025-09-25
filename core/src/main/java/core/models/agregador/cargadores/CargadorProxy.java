package core.models.agregador.cargadores;

import java.util.ArrayList;
import java.util.List;

import core.models.entities.fuentes.Fuente;
import core.api.DTO.HechoAIntegrarDTO;
import core.models.repository.FuentesRepository;
import core.models.agregador.HandlerRecientes;

public class CargadorProxy implements CargadorFuente {

    private static volatile CargadorProxy instance;
    private static HandlerRecientes handlerRecientes = HandlerRecientes.getInstance();
    private  FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    private CargadorProxy() {}

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

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<Fuente> fuentesObtenidas = obtenerFuentes();
        List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

        for (Fuente fuente : fuentesObtenidas) {
            try {
                List<HechoAIntegrarDTO> hechosDeFuente = fuente.extraerHechos();
                if (hechosDeFuente == null) {
                    System.out.println("Fuente " + fuente.getNombre() + " devolvió null, se ignora.");
                    continue;
                }
                for (HechoAIntegrarDTO hecho : hechosDeFuente) {
                    if (handlerRecientes.esReciente(hecho)) {
                        hecho.setTipoFuente("PROXY");
                        hecho.setIdFuente(fuente.getId());
                        hechosAIntegrar.add(hecho);
                    }
                }
                fuente.actualizarUltimoProcesado();
            } catch (Exception e) {
                System.out.println("Error obteniendo de fuente " + fuente.getNombre() + ": " + e.getMessage());
            }
        }
        System.out.println("Total hechos proxy obtenidos: " + hechosAIntegrar.size());
        return hechosAIntegrar;
    }


    public List<Fuente> obtenerFuentes() {
        return fuentesRepository.filtrarFuente("PROXY");
    }

}
