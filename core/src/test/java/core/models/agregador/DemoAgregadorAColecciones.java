package core.models.agregador;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.repository.ColeccionesRepository;
import core.models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;

public class DemoAgregadorAColecciones {
    public static void main(String[] args){
        FuentesRepository fuentesRepository = FuentesRepository.getInstance();
         Fuente fuenteCSV = FuenteFactory.crearFuente2(1, "Desastres Sanitarios", "desastres_sanitarios_contaminacion_argentina.csv", TipoFuente.ESTATICA, TipoConexion.CSV);
            Fuente fuenteAPI = FuenteFactory.crearFuente2(2, "API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
                fuentesRepository.add(fuenteCSV);
                fuentesRepository.add(fuenteAPI);

            List<Fuente> fuentes = new ArrayList<>();
                fuentes.add(fuenteAPI);
                fuentes.add(fuenteCSV);

            ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
            Coleccion coleccionPrueba  = new Coleccion(1, "Todos", "Todos los hechos que existen", null, fuentes, null, null);
                coleccionesRepository.add(coleccionPrueba);


        SchedulerAgregador scheduler = new SchedulerAgregador(false);

        scheduler.iniciarScheduler();
    }




}
