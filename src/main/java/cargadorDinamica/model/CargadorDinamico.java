package cargadorDinamica.model;

import cargadorDinamica.repository.DinamicaRepository;

import java.util.List;

public class CargadorDinamico {

    private static CargadorDinamico instance;
    private final DinamicaRepository dinamicaRepository = DinamicaRepository.getInstance();

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

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        List<HechoAIntegrarDTO> hechos =dinamicaRepository.getHechosNoProcesados();
        hechos.forEach(h -> h.setTipoFuente("DINAMICA"));
        return hechos;
    }
}
