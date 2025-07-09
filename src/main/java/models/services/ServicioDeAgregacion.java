package models.services;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorColecciones;
import models.entities.fuentes.Fuente;
import java.util.*;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;

public class ServicioDeAgregacion {
    private List<Fuente> fuentes; // Es una lista con todas las fuentes de donde va a extraer los hechos
    private List<Coleccion> colecciones; // Una lista con todas las colecciones que hay
    private List<Hecho> hechosCargadosEnLaUltimaHora;

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

    FiltradorColecciones filtradorCriterios = FiltradorColecciones.getInstance();

    HechosRepository hechosRepository = HechosRepository.getInstance();

    private void agregarColeccion(Coleccion nuevaColeccion){
        this.colecciones.add(nuevaColeccion);
    }

    private void agregarFuente(Fuente nuevaFuente){
        this.fuentes.add(nuevaFuente);
    }

    private void obtenerTodosLosHechosNuevos () {
        for (Fuente fuente : fuentes) {
            List<Hecho> lista = fuente.extraerHechosRecientes();
            hechosCargadosEnLaUltimaHora.addAll(lista);
        }
    }

    private void agregarHechosAColecciones()
    {
        for (Coleccion coleccion : colecciones) {
            List<Criterio> criterios = coleccion.getCriterioDePertenencia();
            List<Hecho> hechosFiltrados = filtradorCriterios.filtrarHechos(hechosCargadosEnLaUltimaHora, criterios);
            for(Hecho hecho: hechosFiltrados)
            {
                hechosRepository.add(hecho);
                coleccion.agregarHecho(hecho);
            }
        }
    }

    public void actualizarColecciones()
    {
        obtenerTodosLosHechosNuevos();
        agregarHechosAColecciones();
        hechosCargadosEnLaUltimaHora.clear();
    }

}

