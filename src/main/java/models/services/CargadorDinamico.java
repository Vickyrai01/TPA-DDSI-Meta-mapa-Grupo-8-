package models.services;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.repository.FuentesRepository;
import models.entities.fuentes.Fuente;

import java.util.List;

public class CargadorDinamico {

    private static final TipoFuente tipoConexion = TipoFuente.DINAMICA;

    private static volatile models.services.CargadorDinamico instance;

    public static models.services.CargadorDinamico getInstance() {
        if (instance == null) {
            synchronized (models.services.CargadorDinamico.class) {
                if (instance == null) {
                    instance = new models.services.CargadorDinamico();
                }
            }
        }
        return instance;
    }
    private List<Fuente> fuentesDinamicas;


    public List<Fuente> obtenerFuentes() {
        return FuentesRepository.filtrarFuente(tipoConexion);
    }

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        fuentesObtenidas = obtenerFuentes();
        return ;
    }






}
