package models.services;

import models.entities.fuentes.Fuente;

import java.time.LocalDate;
import java.util.*;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;
import models.entities.normalizador.*;
import models.entities.solicitud.DetectorDeSpam;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

public class ServicioDeAgregacion {
    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();

    private ColeccionesRepository coleccionesRepository = new ColeccionesRepository();
    private HechosRepository hechosRepository = HechosRepository.getInstance();
    private FuentesRepository fuentesRepository = FuentesRepository.getInstance();
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();
    private NormalizadorFecha normalizadorFecha = NormalizadorFecha.getInstance();
    private NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    private FactoryHecho factoryHecho = FactoryHecho.getInstance();

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





    //FLUJO:
    //1.  Obtenemos todos los HechosDTO a integrar de las fuentes, eliminando duplicados fuente a fuente. Pensar un algoritmo.
    //2.  Eliminamos los spam
    //3.  Por cada hecho a integrar verifique los duplicados contra la lista de hechosAIntegrar.
    //    - En caso de haber una coincidencia...elegimos una categoria para ponerle!
    //    - Cranear un poco mas lo de la categoria, onda cual tomamos. -> NormalizadorCategoria
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
                //List<HechoAIntegrarDTO> lista = fuente.extraerHechosRecientes();
                //eliminarSpam(lista);
                //eliminarDuplicados(lista);
                //normalizadorCategoria.estandarizarCategoriasDuplicadas(lista);
                //hechosAIntegrar.addAll(lista);
            }
        }
    }

    private void eliminarSpam(List <HechoAIntegrarDTO> lista){
        lista.removeIf(h -> DetectorDeSpam.esSpam(h.getTitulo()) || DetectorDeSpam.esSpam(h.getDescripcion()));
        }


    public void eliminarDuplicados(List<HechoAIntegrarDTO> hechos) {
        Objects.requireNonNull(hechos, "lista nula");
        for (int i = 0; i < hechos.size(); i++) {
            HechoAIntegrarDTO hi = hechos.get(i);
            for (int j = i + 1; j < hechos.size(); ) {
                if (comparadorHechos.hechoDuplicado(hi, hechos.get(j))) {
                    hechos.remove(j);
                } else {
                    j++; // solo avanzá si no eliminaste
                }
            }
        }
    }

    //1. Buscar los hechos parecidos, varios grupos de hechos parecidos
    //2. Dejamos una lista para los no parecidos
    //3. NormalizadorCategoria: 1 que normaliza normal, la busca en el repo
    //     NormalizadorCategroria que reciba una lista y haga la logica
    //     Si crea una categoria nueva y es solo, se deja o se manda a revisión??

    public void normalizarYCrearHechos() {
        for (HechoAIntegrarDTO dto : hechosAIntegrar) {
            try{
                Categoria categoria = normalizadorCategoria.obtenerCategoria(dto.getCategoria()); //ULTRA PENSAR!!
                LocalDate fecha = normalizadorFecha.normalizarFecha(dto.getFechaDeHecho());
                Hecho hecho = factoryHecho.convertirHecho(dto, fecha, categoria);
            } catch (NormalizadorFecha.ExcepcionRevisionManualFecha e) {
                //Enviar a revisión manual
                throw new RuntimeException(e);
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
        normalizarYCrearHechos();
        for (Coleccion coleccion : colecciones) {
            agregarHechosAColecciones(coleccion);
        }
        hechosAIntegrar.clear();
    }
*/

}



