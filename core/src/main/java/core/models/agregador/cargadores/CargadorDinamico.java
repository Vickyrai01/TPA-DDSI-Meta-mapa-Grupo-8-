package core.models.agregador.cargadores;

import core.models.repository.DinamicaRepository;
import core.models.entities.fuentes.Fuente;
import core.api.DTO.HechoAIntegrarDTO;

import java.util.List;

public class CargadorDinamico implements CargadorFuente {

    private DinamicaRepository dinamicaRepository = DinamicaRepository.getInstance();

    private static volatile CargadorDinamico instance;

    public static CargadorDinamico getInstance() {
        if (instance == null) {
            synchronized (CargadorDinamico.class) {
                if (instance == null) {
                    instance = new CargadorDinamico();
                }
            }
        }
        return instance;
    }

    private List<Fuente> fuentesDinamicas;

    public List<Fuente> obtenerFuentes() {
        return null;
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<HechoAIntegrarDTO> hechos = dinamicaRepository.getHechosNoProcesados();
        hechos.forEach(h -> h.setTipoFuente("DINAMICA"));
        return hechos;
    }
}
