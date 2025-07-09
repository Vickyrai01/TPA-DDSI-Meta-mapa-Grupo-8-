package models.entities.fuentes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorCriterios;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Estado;
import models.entities.hecho.Hecho;
import api.DTO.HechoResponse;
import models.repository.ColeccionesRepository;
import models.repository.HechosRepository;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StrategyAPIREST implements StrategyTipoConexion {

    FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();
    HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public List<Hecho> extraerHecho(List<Criterio> criterios, String fuente){
        List<Hecho> hechosExtraidos = new ArrayList<>();
        WebClient clientUsers = WebClient.create(fuente);

        HechosRepository hechosRepository = HechosRepository.getInstance();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        try {
            Response response = clientUsers
                    .header("Content-Type", "application/json")
                    .get();

            int status = response.getStatus();
            System.out.println("Status: " + status);
            String responseBody = response.readEntity(String.class);

            if (status != 200) {
                throw new RuntimeException("Error en la llamada a /api/user: " + responseBody);
            }

            HechoResponse[] hechos = objectMapper.readValue(responseBody, HechoResponse[].class);

            for (HechoResponse hechoResponse : hechos) {
                Coordenadas coordenada = new Coordenadas(
                        hechoResponse.getLatitud(),
                        hechoResponse.getLongitud()
                );
                Hecho nuevoHecho = new Hecho(
                        hechoResponse.getId(),
                        coordenada,
                        null,
                        null,
                        LocalDate.now(),
                        null,
                        Estado.ACEPTADO,
                        null,
                        LocalDate.now(),
                        hechoResponse.getFechaSuceso(),
                        TipoFuente.PROXY,
                        null,
                        hechoResponse.getDescripcion(),
                        hechoResponse.getTitulo()
                );
                if (filtradorCriterios.cumpleCriterios(nuevoHecho, criterios)){
                    //hechosExtraidos.add(nuevoHecho);
                    hechosRepository.add(nuevoHecho);
                    System.out.println("Hecho: " + nuevoHecho);
                }

            }
            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    };

    @Override
    public List<Hecho> agregarHecho(String FuenteBase, Hecho hecho) {
        //ES UN POST, LO QUE SUBE EL USUARIO
        return null;
    };

    @Override
    public List<Hecho> extraerHechosRecientes(String fuente){
        List<Hecho> hechosExtraidos = new ArrayList<>();
        WebClient clientUsers = WebClient.create(fuente);

        HechosRepository hechosRepository = HechosRepository.getInstance();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        try {
            Response response = clientUsers
                    .header("Content-Type", "application/json")
                    .get();

            int status = response.getStatus();
            System.out.println("Status: " + status);
            String responseBody = response.readEntity(String.class);

            if (status != 200) {
                throw new RuntimeException("Error en la llamada a /api/user: " + responseBody);
            }

            HechoResponse[] hechos = objectMapper.readValue(responseBody, HechoResponse[].class);

            for (HechoResponse hechoResponse : hechos) {
                Coordenadas coordenada = new Coordenadas(
                        hechoResponse.getLatitud(),
                        hechoResponse.getLongitud()
                );
                Hecho nuevoHecho = new Hecho(
                        hechoResponse.getId(),
                        coordenada,
                        null,
                        null,
                        LocalDate.now(),
                        null,
                        Estado.ACEPTADO,
                        null,
                        LocalDate.now(),
                        hechoResponse.getFechaSuceso(),
                        TipoFuente.PROXY,
                        null,
                        hechoResponse.getDescripcion(),
                        hechoResponse.getTitulo()
                );
                if (!hechosRepository.esHechoDuplicado(nuevoHecho)){
                    hechosExtraidos.add(nuevoHecho);
                    //hechosRepository.add(nuevoHecho);
                    System.out.println("Hecho: " + nuevoHecho);
                }

            }
            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
