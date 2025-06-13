package models.entities.hecho;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.Response;

public class ConsumirAPI {
    public void loguearUsuariosId() throws Exception {
        WebClient clientUsers = WebClient.create("https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos");

        ObjectMapper objectMapper = new ObjectMapper();
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
}