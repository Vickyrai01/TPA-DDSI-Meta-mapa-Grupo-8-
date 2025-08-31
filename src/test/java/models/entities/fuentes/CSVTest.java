package models.entities.fuentes;

import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.CriterioNombre;
import models.entities.hecho.Hecho;
import models.entities.normalizador.HechoAIntegrarDTO;
import models.repository.HechosRepository;

import java.io.File;
import java.util.List;

//NO ANDA
public class CSVTest {
    public static void main(String[] args) {
        StrategyCSV strategy = new StrategyCSV();
        Fuente fuente = FuenteFactory.crearFuente("Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        HechosRepository hechosRepository = HechosRepository.getInstance();

        //filtra por emergencia y buenos aires el criterio :)
        CriterioNombre criterioNombre1 = new CriterioNombre("Buenos Aires");
        CriterioNombre criterioNombre2 = new CriterioNombre("emergencia");
        List<Criterio> criterios = List.of(criterioNombre1, criterioNombre2);

        System.out.println("*********DEMO CSV: SOLO MUESTRA LOS PRIMEROS 15**************");
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio");
        System.out.println("Buscando archivo...");
        File archivo = new File(fuente.getLink());
        if (!archivo.exists()) {
            System.out.println("...Archivo no encontrado: " + fuente.getLink());
            return;
        }
        else {System.out.println("...¡Se encontro el archivo!");}
        System.out.println( "                          ");

        List<HechoAIntegrarDTO> todosLosHechos = fuente.extraerHechos(criterios);

        List<HechoAIntegrarDTO> primeros15 = todosLosHechos.stream().limit(15).toList();
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " Hechos en el repositorio");
        System.out.println("Ejemplo de 15 hechos encontrados:");
        System.out.println("************************************************************");

        primeros15.forEach(System.out::println);

    }
    }

