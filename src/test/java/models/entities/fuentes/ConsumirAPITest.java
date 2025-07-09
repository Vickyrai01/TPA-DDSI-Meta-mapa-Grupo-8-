package models.entities.fuentes;

import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.CriterioFechaSuceso;
import models.repository.HechosRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumirAPITest {
    public static void main(String[] args) throws Exception {

        Fuente fuente = FuenteFactory.crearFuente("API de ejemplo","https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",TipoFuente.PROXY, TipoConexion.APIREST);
        HechosRepository hechosRepository = HechosRepository.getInstance();

        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos en el repositorio");
        System.out.println("Extrayendo hechos de API...");

        LocalDate fechaInicio = LocalDate.of(2023, 4,30);
        LocalDate fechaFin = LocalDate.of(2023, 6, 30);
        CriterioFechaSuceso fechaSuceso = new CriterioFechaSuceso(fechaInicio, fechaFin);
        List<Criterio> criterios = new ArrayList<>(List.of(fechaSuceso));


        fuente.extraerHechos(criterios);
        System.out.println("Hay " + hechosRepository.obtenerTodas().size() + " hechos nuevos en el repositorio");

    }
}
