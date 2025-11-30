package utils;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Locale;

public class GeocodingUtils {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse";

    public static String obtenerProvincia(double lat, double lon) {
        try {
            Thread.sleep(100);

            try (CloseableHttpClient client = HttpClients.createDefault()) {
                // Usar Locale.US para asegurar que se use punto como separador decimal
                String url = String.format(Locale.US, "%s?format=json&lat=%f&lon=%f", NOMINATIM_URL, lat, lon);
                HttpGet request = new HttpGet(url);
                request.setHeader("User-Agent", "MetamapaApp/1.0");

                String response = EntityUtils.toString(client.execute(request).getEntity());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response);

                JsonNode address = root.get("address");
                if (address != null) {

                    String[] posiblesCampos = {"state", "state_district", "province", "county"};

                    for (String campo : posiblesCampos) {
                        JsonNode province = address.get(campo);
                        if (province != null && !province.asText().isEmpty()) {
                            return province.asText();
                        }
                    }
                } else {
                    System.out.println("No se encontró el objeto 'address' en la respuesta");
                }
                return "Desconocida";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Desconocida";
        }
    }
}
