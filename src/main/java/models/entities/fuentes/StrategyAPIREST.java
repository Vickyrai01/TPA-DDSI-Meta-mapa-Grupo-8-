package models.entities.fuentes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorCriterios;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Estado;
import models.entities.hecho.Hecho;
import api.dto.HechoDTO;
import models.entities.normalizador.HechoAIntegrarDTO;
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
    public List<HechoAIntegrarDTO> extraerHecho(List<Criterio> criterios, String fuente, String codigoFuente){
        List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();
        WebClient clientUsers = WebClient.create(fuente);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            Response response = clientUsers
                    .header("Content-Type", "application/json")
                    .get();

            int status = response.getStatus();
            String responseBody = response.readEntity(String.class);

            if (status != 200) {
                throw new RuntimeException("Error en la llamada a /api/user: " + responseBody);
            }

            HechoAIntegrarDTO[] array = objectMapper.readValue(responseBody, HechoAIntegrarDTO[].class);

            for (HechoAIntegrarDTO dto : array) {
                // si necesitás completar campos que no vienen del JSON:
                // dto.setCategoria(normalizadorCategoria.inferir(...) o null);
                // dto.setEtiquetas(...);

              //  if (filtradorCriterios.cumpleCriterios(dto, criterios)) {
                    hechosExtraidos.add(dto);
                    System.out.println("Hecho filtrado: " + dto.getTitulo());
               // }
            }

            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }};

    @Override
    public List<HechoAIntegrarDTO> extraerHechosRecientes(String fuente,  String codigoFuente){
        List<HechoAIntegrarDTO> hechosExtraidos = new ArrayList<>();
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

            HechoDTO[] hechos = objectMapper.readValue(responseBody, HechoDTO[].class);

            for (HechoDTO hechoResponse : hechos) {
                Coordenadas coordenada = new Coordenadas(
                        hechoResponse.getLatitud(),
                        hechoResponse.getLongitud()
                );
                Hecho nuevoHecho = new Hecho( //CAMBIAR A HECHOAINTEGRARDTO
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
                        hechoResponse.getTitulo(),
                        codigoFuente
                );

               /* //VER POR ULTIMA ACTUALIZACION DE LA FUENTE AGREGAR TODO SLOS
                if (!hechosRepository.esHechoDuplicado(nuevoHecho)){
                    hechosExtraidos.add(nuevoHecho);
                    hechosRepository.add(nuevoHecho);
                    System.out.println("Hecho: " + nuevoHecho);
                }*/
            }
            return hechosExtraidos;


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}