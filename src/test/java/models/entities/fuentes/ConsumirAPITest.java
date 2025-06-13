package models.entities.fuentes;

import models.repository.HechosRepository;

public class ConsumirAPITest {
    public static void main(String[] args) throws Exception {

        final HechosRepository repoHechos = HechosRepository.getInstance();
        StrategyAPIREST consumirAPI = new StrategyAPIREST();

        consumirAPI.extraerHecho(null, "https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos");

    }
}
