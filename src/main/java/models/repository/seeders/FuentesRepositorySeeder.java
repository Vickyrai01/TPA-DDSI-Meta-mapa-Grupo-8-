package models.repository.seeders;

import models.entities.fuentes.*;
import models.repository.FuentesRepository;
import models.repository.HechosRepository;

import java.net.URL;

public class FuentesRepositorySeeder {

    private static volatile FuentesRepositorySeeder instance;

    public FuentesRepositorySeeder() {
    }


    public static FuentesRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (FuentesRepositorySeeder.class) {
                if (instance == null) {
                    instance = new FuentesRepositorySeeder();
                }
            }
        }
        return instance;
    }

    FuentesRepository fuentesRepository = FuentesRepository.getInstance();

    StrategyCSV strategyCSV = new StrategyCSV(); //no se si esta bien :P
    StrategyAPIREST strategyAPIREST = new StrategyAPIREST();

    Fuente fuente1 = new Fuente(1, "CSV incendio", null, TipoFuente.ESTATICA, strategyCSV);
    Fuente fuente2 = new Fuente(2, "API choque", null, TipoFuente.PROXY, strategyAPIREST);
    Fuente fuente3 = new Fuente(3, "Contribucion", null, TipoFuente.DINAMICA, null);

    public void cargarFuentesSeeder()
    {
        fuentesRepository.add(fuente1);
        fuentesRepository.add(fuente2);
        fuentesRepository.add(fuente3);
    }


}
