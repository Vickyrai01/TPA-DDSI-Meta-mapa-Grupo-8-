package models.agregador.cargadores;

import java.util.ArrayList;
import java.util.List;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.HechoAIntegrarDTO;
import models.repository.FuentesRepository;
import models.agregador.HandlerRecientes;

public class CargadorProxy implements CargadorFuente {

    private static volatile CargadorProxy instance;

    private static final TipoFuente tipoConexion = TipoFuente.PROXY;
    private static HandlerRecientes handlerRecientes = HandlerRecientes.getInstance();

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

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() { //CAMBIAR TIPO A FUENTES DTO
        List<Fuente> fuentesObtenidas = obtenerFuentes();
        List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

        for (Fuente fuente : fuentesObtenidas) {
            List<HechoAIntegrarDTO> hechosDeFuente = fuente.extraerHechos();
            for (HechoAIntegrarDTO hecho : hechosDeFuente) {
                if (handlerRecientes.esReciente(hecho)) {
                    hechosAIntegrar.add(hecho);
                }
            }
            fuente.actualizarUltimoProcesado();
        }
        return hechosAIntegrar;
    }

    public List<Fuente> obtenerFuentes() {
        return FuentesRepository.filtrarFuente(tipoConexion);
    }

}
