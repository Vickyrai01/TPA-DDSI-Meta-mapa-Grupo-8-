package models.services;

import java.util.List;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.entities.hecho.HechoAIntegrarDTO;
import models.services.ServicioDeAgregacion;
import models.repository.FuentesRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CargadorProxy {

    private static volatile CargadorProxy instance;

    private static final TipoFuente tipoConexion = TipoFuente.PROXY;

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

    /*
    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() {
        List<Fuente> fuentesObtenidas = obtenerFuentes();

        List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

        for(Fuente fuente : fuentesObtenidas){

            hechosAIntegrar.addAll(fuente.extraerHechosRecientes());
        }

        return hechosAIntegrar;
    }
    */

    public List<Hecho> extraerHechosAIntegrar() {
        List<Fuente> fuentesObtenidas = obtenerFuentes();

        List<Hecho> hechosAIntegrar = fuentesObtenidas.stream()
                .flatMap(fuente -> fuente.extraerHechosRecientes().stream()) // aplana todas las listas
                .collect(Collectors.toList()); // junta todo en una lista

        return hechosAIntegrar;
    }

    private List<Fuente> obtenerFuentes() {
        return FuentesRepository.filtrarFuente(tipoConexion);
    }


}
