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
        Fuente fuenteCore = FuenteFactory.crearFuente("Hechos Reportados", null,  TipoFuente.DINAMICA, TipoConexion.DINAMICA);
        fuentesRepository.add(fuenteCore);
    }


}
