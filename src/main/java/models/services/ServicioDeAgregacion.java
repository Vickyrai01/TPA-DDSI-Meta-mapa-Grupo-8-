package models.services;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorColecciones;
import models.entities.fuentes.Fuente;
import java.util.*;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

public class ServicioDeAgregacion {
    //private List<Fuente> fuentes = new ArrayList<>(); // Es una lista con todas las fuentes de donde va a extraer los hechos, esto muere
    //private List<Coleccion> colecciones = new ArrayList<>(); // Una lista con todas las colecciones que hay

    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private ColeccionesRepository coleccionesRepository = new ColeccionesRepository();
    private static volatile ServicioDeAgregacion instance;

    private ServicioDeAgregacion() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static ServicioDeAgregacion getInstance() {
        if (instance == null) {
            synchronized (ServicioDeAgregacion.class) {
                if (instance == null) {
                    instance = new ServicioDeAgregacion();
                }
            }
        }
        return instance;
    }

    //FiltradorColecciones filtradorCriterios = FiltradorColecciones.getInstance();

    HechosRepository hechosRepository = HechosRepository.getInstance();

    //1. OBTENEMOS LOS HECHOS A INTEGRAR
/*
    private void obtenerTodosLosHechosNuevos () {
        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        List<Fuente> fuentes = fuentesRepository.obtenerTodas();
        for (Fuente fuente : fuentes) {
            List<HechoAIntegrarDTO> lista = fuente.extraerHechosRecientes();
            hechosAIntegrar.addAll(lista);
        }
    }     */

    //FLUJO:
//Un metodo que por cada hecho a integrar verifique los duplicados contra la lista de hechosAIntegrar.
//En caso de haber una coincidencia...elegimo una categoria para ponerle!
//Cranear un poco mas lo de la categoria, onda cual tomamos. -> NormalizadorCategoria
//normalizar la fecha
// Enviar al factory


/*
    private void agregarHechosAColecciones(Coleccion coleccion)
    {
            List<Criterio> criterios = coleccion.getCriterioDePertenencia();
            List<Hecho> hechosFiltrados = filtradorCriterios.filtrarHechos(hechosAIntegrar, criterios);
            for (Hecho hecho : hechosFiltrados) {
                if (hecho.perteneceAFuente(coleccion.extraerCodigosDeFuentes(fuentes))) {
                    coleccion.agregarHecho(hecho);
                    hechosRepository.add(hecho);
                }
            }
    }


    public void actualizarColecciones()
    {
        obtenerTodosLosHechosNuevos();
        for (Coleccion coleccion : colecciones) {
            agregarHechosAColecciones(coleccion);
        }
        hechosAIntegrar.clear();
    }
 */ //CAMBIAR A HECHO A INTEGRAR DTO

    /*
    public void evaluarDuplicado(HechoAIntegrarDTO hecho, List<HechoAIntegrarDTO> hechos) {
        String titulo = ponerEnMinuscula(hecho.titulo); //MINUSCULA Y SACARLE LOS ARTICULOS
        if(existeAlgunHechoMismoTitulo(titulo, hechos)){
            //verificar cual reemplazar
            for(HechoAIntegrarDTO hechoAIntegrar : hechos) {
                if(hecho.tieneMismoTitulo(hechoAIntegrar.getTitulo())){
                    if()
                }
            }
        }
        else if(coincidenAtributos(hecho, hechos)){
            //obtener el hecho que coincide los atributos y verificar cual reemplazar
        }
        else{
            //normalizarlo y crearlo
        }
    }
*/

    public Boolean existeAlgunHechoMismoTitulo(String titulo, List<HechoAIntegrarDTO> hechos){
        return hechos.stream().anyMatch(h -> h.tieneMismoTitulo(titulo));
        //Si existe algun hecho con el mismo titulo del pasado por parametro (sin tener en cuenta mayusculas)
    }

    public Boolean coincidenAtributos(HechoAIntegrarDTO hecho, List<HechoAIntegrarDTO> hechos){
        //implementar porcentaje de coincidencia??;
        return false;
    }
}



