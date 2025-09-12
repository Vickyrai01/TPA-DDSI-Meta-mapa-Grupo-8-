package models.entities.agregador;

import models.agregador.SchedulerAgregador;
import models.agregador.ServicioDeAgregacion;
import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.CriterioDescripcion;
import models.entities.colecciones.criterios.CriterioNombre;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.FuenteFactory;
import models.entities.fuentes.TipoConexion;
import models.entities.fuentes.TipoFuente;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.List;

public class DemoAgregadorConActualizacion {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        SchedulerAgregador scheduler = new SchedulerAgregador(false);

        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        Fuente fuenteCSV = FuenteFactory.crearFuente("Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuenteAPI = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        fuentesRepository.add(fuenteCSV);
        fuentesRepository.add(fuenteAPI);


        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuenteCSV);
        fuentes.add(fuenteAPI);

        ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
        Coleccion coleccionPrueba  = new Coleccion(1, "Todos los hechos", "", null, fuentes, null, null);
        coleccionesRepository.add(coleccionPrueba);

        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("La coleccion " + coleccionPrueba.toString() + " tiene " + coleccionPrueba.getHechos().size() + " hechos");
        System.out.println("----------");
        scheduler.iniciarScheduler();

        //System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " nuevos hechos en el repositorio");
        //System.out.println("La coleccion " + coleccionPrueba.toString() + " tiene " + coleccionPrueba.getHechos().size() + " hechos");

    }

}
