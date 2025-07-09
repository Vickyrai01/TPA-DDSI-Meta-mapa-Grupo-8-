package models.entities.services;


import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.CriterioCategoria;
import models.entities.colecciones.criterios.CriterioDescripcion;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.StrategyCSV;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;
import models.services.ServicioDeAgregacion;

import java.util.ArrayList;
import java.util.List;

public class DemoAgregador {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        Categoria categoria = new Categoria("Contaminación");
        CriterioCategoria criterioCategoria = new CriterioCategoria(categoria);
        CriterioDescripcion descripcion = new CriterioDescripcion("Fabrica");

        List<Criterio> criterios = new ArrayList<>(List.of(criterioCategoria, descripcion));

        List<Hecho> coleccionHechos1 = new ArrayList<>();

        Coleccion coleccion1 = new Coleccion(1, "Contaminacion","Contaminacion por fabricas en Argentina", criterios,coleccionHechos1,null );
        servicioDeAgregacion.agregarColeccion(coleccion1);

        StrategyCSV strategyCSV = new StrategyCSV();
        Fuente fuente1 = new Fuente(1, "CSV contaminacion 1", "eventosSanitariosPrueba1.csv", TipoFuente.ESTATICA, strategyCSV);
        servicioDeAgregacion.agregarFuente(fuente1);

        System.out.println("**************PRUEBA SERVICIO AGREGADOR**************************");
        System.out.println("");
        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size());
        System.out.println("--actualizando fuente...--");
        servicioDeAgregacion.actualizarColecciones();

        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size() );
        Fuente fuente2 = new Fuente(2, "CSV contaminacion 2", "eventosSanitariosPrueba2.csv", TipoFuente.ESTATICA, strategyCSV);
        System.out.println("--actualizando fuente...--");
        servicioDeAgregacion.agregarFuente(fuente2);
        servicioDeAgregacion.actualizarColecciones();

        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size() );
        Fuente fuente3 = new Fuente(3, "CSV contaminacion 3", "eventosSanitariosPrueba3.csv", TipoFuente.ESTATICA, strategyCSV);
        System.out.println("--actualizando fuente...--");
        servicioDeAgregacion.agregarFuente(fuente3);
        servicioDeAgregacion.actualizarColecciones();

        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size());

    }
}
