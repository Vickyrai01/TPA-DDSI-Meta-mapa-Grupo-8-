package models.entities.agregador;

import models.agregador.ServicioDeAgregacion;
import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.FuenteFactory;
import models.entities.fuentes.TipoConexion;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Hecho;
import models.repository.ColeccionesRepository;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemoAgregadorCasoInicial {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        Fuente fuenteCSV = FuenteFactory.crearFuente("Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuenteAPI = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        fuentesRepository.add(fuenteCSV);
        fuentesRepository.add(fuenteAPI);


        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuenteCSV);

        ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
        Coleccion coleccionPrueba  = new Coleccion(1, "Todos", "Todos los hechos que existen", null, fuentes, null, null);
        coleccionesRepository.add(coleccionPrueba);


        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");
        servicioDeAgregacion.actualizarColecciones();


        Hecho random1 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random1.getTitulo()  + "- Tipo de Fuente: " + random1.getFuenteDeOrigen());
        Hecho random2 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random2.getTitulo()  + "- Tipo de Fuente: " + random2.getFuenteDeOrigen());
        Hecho random3 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random3.getTitulo()  + "- Tipo de Fuente: " + random3.getFuenteDeOrigen());
        Hecho random4 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random4.getTitulo()  + "- Tipo de Fuente: " + random4.getFuenteDeOrigen());
        Hecho random5 = hechosRepository.obtenerTodas().get(new Random().nextInt(hechosRepository.obtenerTodas().size()));
        System.out.println("Hecho random: " + random5.getTitulo()  + "- Tipo de Fuente: " + random5.getFuenteDeOrigen());
    }
}
