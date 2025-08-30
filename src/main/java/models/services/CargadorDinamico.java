package models.services;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.repository.FuentesRepository;
import models.entities.fuentes.Fuente;
import models.entities.HechoAIntegrarDTO.HechoAIntegrarDTO;
import java.util.ArrayList;
import java.util.List;

public class CargadorDinamico implements CargadorFuente{

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
        return FuentesRepository.getInstance().filtrarFuente(tipoConexion); //VER GETINSTANCE()
    }

    public List<Hecho> extraerHechosAIntegrar() { //CAMBIAR TIPO A FUENTES DTO
        List<Fuente> fuentesObtenidas = obtenerFuentes();
        List<Hecho> hechosAIntegrar = new ArrayList<>();

        for (Fuente fuente : fuentesObtenidas) {
            List<Hecho> hechosDeFuente = fuente.extraerHechosRecientes();
            //public List<Hecho> extraerHechosRecientes(String fuente,  String codigoFuente)

            hechosAIntegrar.addAll(hechosDeFuente);
        }
        return hechosAIntegrar;
    }
}
