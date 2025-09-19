package core.models.agregador.cargadores;

//import javax.ws.rs.core.Response;


public class HandlerCargadores {


    private static volatile HandlerCargadores instance;

    private HandlerCargadores() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static HandlerCargadores getInstance() {
        if (instance == null) {
            synchronized (HandlerCargadores.class) {
                if (instance == null) {
                    instance = new HandlerCargadores();
                }
            }
        }
        return instance;
    }
    /*
    String dinamico = ConfigLoader.getProperty("CargadorDinamico");
    String proxy = ConfigLoader.getProperty("CargadorProxy");
    String estatico = ConfigLoader.getProperty("CargadorEstatico");
    List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();

    public List<HechoAIntegrarDTO> extraerHechosAIntegrar(){
        hechosAIntegrar.addAll(extraerHecho(dinamico));
        hechosAIntegrar.addAll(extraerHecho(proxy));
        hechosAIntegrar.addAll(extraerHecho(estatico));
        return hechosAIntegrar;
    }

    public List<HechoAIntegrarDTO> extraerHecho(String fuente){
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

            for (HechoAIntegrarDTO dto: array) {
                hechosExtraidos.add(dto);
            }

            return hechosExtraidos;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }};

     */
}
