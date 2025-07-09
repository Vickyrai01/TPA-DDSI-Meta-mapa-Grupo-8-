package models.repository.seeders;

import models.entities.fuentes.*;
import models.repository.FuentesRepository;

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

    public void cargarFuentesSeeder()
    {
        Fuente fuente1 = FuenteFactory.crearFuente("CSV Incendio", null,  TipoFuente.ESTATICA, TipoConexion.CSV);
        Fuente fuente2 = FuenteFactory.crearFuente("API Choque", null,  TipoFuente.PROXY, TipoConexion.APIREST);
        Fuente fuente3 = FuenteFactory.crearFuente("Contribucion", null,  TipoFuente.DINAMICA, null);
        fuentesRepository.add(fuente1);
        fuentesRepository.add(fuente2);
        fuentesRepository.add(fuente3);
    }


}
