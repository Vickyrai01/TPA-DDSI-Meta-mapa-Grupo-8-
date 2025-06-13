package models.entities.fuentes;

import models.entities.hecho.ConsumirAPI;
import models.repository.HechosRepository;

public class ConsumirAPITest {
    public static void main(String[] args) throws Exception {

        final HechosRepository repoHechos = HechosRepository.getInstance();
        ConsumirAPI consumirAPI = new ConsumirAPI();

        //consumirAPI.loguearUsuariosId();

        //consumirAPI.loguearTitulos();

        consumirAPI.loguearHechos();
        System.out.println("****************Hechos guardados:**********************");
        repoHechos.obtenerTodas().forEach(System.out::println);

    }
}
