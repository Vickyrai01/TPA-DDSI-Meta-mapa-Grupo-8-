package models.agregador.cargadores;

import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import api.dto.HechoAIntegrarDTO;
import models.repository.FuentesRepository;
import models.agregador.HandlerRecientes;

import java.util.ArrayList;
import java.util.List;

public class CargadorEstatico implements CargadorFuente {

    private static volatile CargadorEstatico instance;
    private static final TipoFuente tipoConexion = TipoFuente.ESTATICA;
    private static HandlerRecientes handlerRecientes = HandlerRecientes.getInstance();
    private  FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    
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
                    hecho.setTipoFuente("ESTATICA");
                    hecho.setIdFuente(fuente.getId());
                    hechosAIntegrar.add(hecho);
                }
            }
            fuente.actualizarUltimoProcesado();
        }
        System.out.println("Total hechos estaticos obtenidos: " + hechosAIntegrar.size());
        return hechosAIntegrar;
    }

    public List<Fuente> obtenerFuentes() {
        return fuentesRepository.filtrarFuente(tipoConexion);
    }


}
