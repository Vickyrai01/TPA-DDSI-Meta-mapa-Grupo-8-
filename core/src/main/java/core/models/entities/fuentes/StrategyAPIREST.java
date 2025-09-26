package core.models.entities.fuentes;

import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.repository.HechosRepository;

import java.util.List;

public class StrategyAPIREST implements StrategyTipoConexion {

    FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();
    HechosRepository hechosRepository = HechosRepository.getInstance();

    @Override
    public List<HechoAIntegrarDTO> extraerHecho(String fuente, String codigoFuente){
        /*
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
                    hechosExtraidos.add(dto);
            }

            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();



        }*/
        return List.of();
    };
    @Override
    public String devolverTipoDeConexion(){
        return "APIREST";
    };


}