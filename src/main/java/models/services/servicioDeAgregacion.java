package models.services;

import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import java.util.*;
import models.entities.hecho.Hecho;

public class servicioDeAgregacion {
    private List<Fuente> fuentes; // Es una lista con todas las fuentes de donde va a extraer los hechos
    private List<Coleccion> colecciones; // Una lista con todas las colecciones que hay
    private List<Hecho> hechosCargadosEnLaUltimaHora;

    //si creamos una coleccion nueva, se le deben agregar todos los hechos historicos que fueron extraidos? o se empieza a llenar con los hechos nuevos que van llegando


    private void incorporarHechosAColecciones(){
       // this.colecciones
         //       .forEach(unaColeccion -> unaColeccion.agregarHechosSiCumplenCriterio(this.hechosCargadosEnLaUltimaHora));
    }

    private void actualizarHechos(){
      //  this.hechosCargadosEnLaUltimaHora = this.fuentes()
    }

    private void agregarColeccion(Coleccion nuevaColeccion){
        this.colecciones.add(nuevaColeccion);
    }

    private void agregarFuente(Fuente nuevaFuente){
        this.fuentes.add(nuevaFuente);
    }

    //private Collection<Hecho> hechosGlobales; // Donde se guardan todos los hechos agregados
    //private Map<Fuente, LocalDateTime> ultimaIntegracionPorFuente;

    // 1. POR CADA COLECCION QUE TIENE BUSCA LOS HECHOS NUEVOS DE LA FUENTE CON EL FILTRADO CON LOS CRITERIOS
    // 2. AGREGAR LOS HECHOS A LAS COLECCIONES

    //COLECCION -> SU LISTA DE FUENTES -> POR CADA LISTA DE FUENTES OBTIENE LOS HECHOS


    //FILTRADORCRITERIOS
    /*  public Boolean cumpleCriterios(Hecho hecho, List<Criterio> criterios) {
        if(criterios == null || criterios.isEmpty())
        {return true;}
        else{
        return criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));}
    }*/
}

