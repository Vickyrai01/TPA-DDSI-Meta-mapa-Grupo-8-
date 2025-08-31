package models.services;

import java.util.ArrayList;
import java.util.List;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.repository.FuentesRepository;


import java.util.stream.Collectors;

public class CargadorProxy implements CargadorFuente{

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


    public List<HechoAIntegrarDTO> extraerHechosAIntegrar() { //CAMBIAR TIPO A FUENTES DTO
        List<Fuente> fuentesObtenidas = obtenerFuentes();
        List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

        for (Fuente fuente : fuentesObtenidas) {
            List<HechoAIntegrarDTO> hechosDeFuente = fuente.extraerHechosRecientes();
            //public List<Hecho> extraerHechosRecientes(String fuente,  String codigoFuente)
            hechosAIntegrar.addAll(hechosDeFuente);
            fuente.actualizarUltimoProcesado();
        }
        return hechosAIntegrar;}

    public List<Fuente> obtenerFuentes() {
        return FuentesRepository.filtrarFuente(tipoConexion);
    }


}
