package models.services;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorColecciones;
import models.entities.fuentes.Fuente;
import java.util.*;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.entities.normalizador.ComparadorHechos;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.entities.solicitud.DetectorDeSpam;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

public class ServicioDeAgregacion {
    //private List<Fuente> fuentes = new ArrayList<>(); // Es una lista con todas las fuentes de donde va a extraer los hechos, esto muere
    //private List<Coleccion> colecciones = new ArrayList<>(); // Una lista con todas las colecciones que hay

    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();
    private ColeccionesRepository coleccionesRepository = new ColeccionesRepository();
    private FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private static volatile ServicioDeAgregacion instance;
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();


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

    //FLUJO:
    //1.  Obtenemos todos los HechosDTO a integrar de las fuentes, eliminando duplicados fuente a fuente. Pensar un algoritmo.
    //2.  Eliminamos los spam
    //3.  Por cada hecho a integrar verifique los duplicados contra la lista de hechosAIntegrar.
    //  - En caso de haber una coincidencia...elegimos una categoria para ponerle!
    //    Cranear un poco mas lo de la categoria, onda cual tomamos. -> NormalizadorCategoria
    // 4. Normalizar la fecha
    //  - 4.1 si no se puede normalizar se manda a revisión manual
    // 5. Enviar al Factory para crear el hecho
    // 6. Agregar a las colecciones correspondientes (ver lo de los criterios de pertenencia)

    private void obtenerTodosLosHechosNuevos () {
        List<Fuente> fuentes = fuentesRepository.obtenerTodas();
        for (Fuente fuente : fuentes) {
            if(fuente.getTipoFuente().equals(TipoFuente.DINAMICA)){
                List<Hecho> listaHechos = fuente.extraerHechosRecientes();
                hechosLimpios.addAll(listaHechos);
            } else {
                List<HechoAIntegrarDTO> lista = fuente.extraerHechosRecientes();
                eliminarSpam(lista);
                eliminarDuplicados(lista);
                hechosAIntegrar.addAll(lista);
            }
        }
    }

    private void eliminarSpam(List <HechoAIntegrarDTO> lista){
        for(HechoAIntegrarDTO hecho : lista){
            if(DetectorDeSpam.esSpam(hecho.getTitulo()) || DetectorDeSpam.esSpam(hecho.getDescripcion())){
                lista.remove(hecho);
            }
        }
    }

    public void eliminarDuplicados(List<HechoAIntegrarDTO> hechos) {
        Objects.requireNonNull(hechos, "lista nula");
        int n = hechos.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (comparadorHechos.esElMismoHecho(hechos.get(i), hechos.get(j))) {
                    hechos.remove(j);
                }
            }
        }
    }

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



