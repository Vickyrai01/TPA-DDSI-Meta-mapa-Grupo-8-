package models.entities.services;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.CriterioCategoria;
import models.entities.colecciones.criterios.CriterioDescripcion;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.FuenteFactory;
import models.entities.fuentes.TipoConexion;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;
import models.services.ServicioDeAgregacion;

import java.util.ArrayList;
import java.util.List;

public class DemoAgregoUnaNuevaFuente {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        //PROBEMOS QUE SE AGREGAN A LA CATEGORIA SI SON DE UNA FUENTE!!!
        Categoria categoria = new Categoria("Contaminación");
        CriterioCategoria criterioCategoria = new CriterioCategoria(categoria);
        CriterioDescripcion descripcion = new CriterioDescripcion("Fabrica");

        List<Criterio> criterios = new ArrayList<>(List.of(criterioCategoria, descripcion));

        List<Hecho> coleccionHechos1 = new ArrayList<>();

        Fuente fuente1 = FuenteFactory.crearFuente("CSV contaminacion 1", "eventosSanitariosPrueba1.csv", TipoFuente.ESTATICA, TipoConexion.CSV);

        List<Fuente> fuentes = new ArrayList<>(List.of(fuente1));

        Coleccion coleccion1 = new Coleccion(1, "Contaminacion","Contaminacion por fabricas en Argentina", criterios, fuentes, coleccionHechos1,null );

        servicioDeAgregacion.agregarColeccion(coleccion1);

        servicioDeAgregacion.agregarFuente(fuente1);

        System.out.println("**************PRUEBA SERVICIO AGREGADOR**************************\n");
        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size());
        System.out.println("--actualizando fuente...--");
        servicioDeAgregacion.actualizarColecciones();
        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size());
        System.out.println("Se crea una nueva fuente!!!\n");

        Fuente fuente2 = FuenteFactory.crearFuente("CSV contaminacion 2", "eventosSanitariosPrueba2.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        coleccion1.agregarFuente(fuente2);
        servicioDeAgregacion.agregarFuente(fuente2);

        servicioDeAgregacion.actualizarColecciones();

        System.out.println("La cantidad de hechos que hay en la coleccion es: " + coleccion1.getHechos().size());
    }
}
