package core.models.repository.seeders;

import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.FuenteFactory;
import core.models.entities.fuentes.TipoConexion;
import core.models.entities.fuentes.TipoFuente;
import core.models.repository.FuentesRepository;

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
        Fuente fuente2 = FuenteFactory.crearFuente("API Choque", "https://choques.com",  TipoFuente.PROXY, TipoConexion.APIREST);
        Fuente fuente3 = FuenteFactory.crearFuente("Contribucion",null ,  TipoFuente.DINAMICA, TipoConexion.APIREST);
        fuentesRepository.add(fuente1);
        fuentesRepository.add(fuente2);
        fuentesRepository.add(fuente3);
    }


}
