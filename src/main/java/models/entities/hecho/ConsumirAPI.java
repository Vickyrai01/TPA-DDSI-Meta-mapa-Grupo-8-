package models.entities.hecho;

import api.clasesResponse.HechoResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.entities.fuentes.TipoFuente;
import models.repository.HechosRepository;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;
import java.time.LocalDate;

public class ConsumirAPI {
    public void loguearUsuariosId() throws Exception {

        WebClient clientUsers = WebClient.create("https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Response response = clientUsers
                .header("Content-Type", "application/json")
                .get();

        int status = response.getStatus();
        System.out.println("Status: " + status);
        String responseBody = response.readEntity(String.class);
        if (status == 200) {
            System.out.println("response = " + responseBody);
            HechoResponse[] hechos = objectMapper.readValue(responseBody, HechoResponse[].class);

            for (int i = 0; i < hechos.length; i++) {
                System.out.println("ID: " + hechos[i].getId());
            }

        } else {
            System.out.println("Error response = " + responseBody);
            throw new Exception("Error en la llamada a /api/user");
        }
    }

    public void loguearHechos() throws Exception {
        final HechosRepository repoHechos = HechosRepository.getInstance();
        WebClient clientUsers = WebClient.create("https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Response response = clientUsers
                .header("Content-Type", "application/json")
                .get();

        int status = response.getStatus();
        System.out.println("Status: " + status);
        String responseBody = response.readEntity(String.class);
        if (status == 200) {
            System.out.println("response = " + responseBody);
            HechoResponse[] hechos = objectMapper.readValue(responseBody, HechoResponse[].class);

            for (int i = 0; i < hechos.length; i++) {
                Coordenadas coordenada = new Coordenadas( hechos[i].getLatitud(), hechos[i].getLongitud());
                Hecho nuevoHecho = new Hecho(
                        hechos[i].getId(),
                        coordenada,
                        null,
                        null,
                        LocalDate.now(),
                        null,
                        Estado.ACEPTADO,
                        null,
                        LocalDate.now(),
                        hechos[i].getFechaSuceso(),
                        TipoFuente.PROXY,
                        null,
                        hechos[i].getDescripcion(),
                        hechos[i].getTitulo()
                );
                repoHechos.add(nuevoHecho);
                System.out.println("Hecho: " + nuevoHecho);
            }

        } else {
            System.out.println("Error response = " + responseBody);
            throw new Exception("Error en la llamada a /api/user");
        }
    }

    public void loguearTitulos() throws Exception {
        WebClient clientUsers = WebClient.create("https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Response response = clientUsers
                .header("Content-Type", "application/json")
                .get();

        int status = response.getStatus();
        System.out.println("Status: " + status);
        String responseBody = response.readEntity(String.class);
        if (status == 200) {
            System.out.println("response = " + responseBody);
            HechoResponse[] hechos = objectMapper.readValue(responseBody, HechoResponse[].class);

            for (int i = 0; i < hechos.length; i++) {
                System.out.println("Título: " + hechos[i].getTitulo());
            }

        } else {
            System.out.println("Error response = " + responseBody);
            throw new Exception("Error en la llamada a /api/user");
        }
    }
}