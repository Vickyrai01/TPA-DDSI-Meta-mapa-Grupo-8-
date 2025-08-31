package models.services;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico implements CargadorFuente {

    private static volatile CargadorEstatico instance;
    private static final TipoFuente tipoConexion = TipoFuente.ESTATICA;
    private static HandlerRecientes handlerRecientes = HandlerRecientes.getInstance();
    
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
