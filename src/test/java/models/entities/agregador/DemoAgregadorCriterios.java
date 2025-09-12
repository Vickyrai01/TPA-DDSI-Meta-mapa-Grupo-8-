package models.entities.agregador;

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

public class DemoAgregadorCriterios {
    public static void main(String[] args){
        ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();

        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
        Fuente fuenteCSV = FuenteFactory.crearFuente("Desastres Sanitarios", "eventosSanitariosPrueba1.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuenteAPI = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        fuentesRepository.add(fuenteCSV);
        fuentesRepository.add(fuenteAPI);


        List<Fuente> fuentes = new ArrayList<>();
        fuentes.add(fuenteCSV);
        fuentes.add(fuenteAPI);

        CriterioNombre criterioNombre1 = new CriterioNombre("Buenos Aires");
        CriterioDescripcion criterioNombre2 = new CriterioDescripcion("emergencia");
        List<Criterio> criterios = List.of(criterioNombre1, criterioNombre2);


        ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
        Coleccion coleccionPrueba  = new Coleccion(1, "Emergencia en Buenos Aires", "", criterios, fuentes, null, null);
        coleccionesRepository.add(coleccionPrueba);


        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");
        servicioDeAgregacion.actualizarColecciones();
       // System.out.println("**La coleccion " + coleccionPrueba.toString() + " tiene " + coleccionPrueba.getHechos().size() + " hechos**");
       // System.out.println("**Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio**");

    }
}
