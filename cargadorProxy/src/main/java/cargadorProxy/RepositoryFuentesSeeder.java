package cargadorProxy;


import cargadorProxy.model.StrategyAPIREST;
import cargadorProxy.model.StrategyTipoConexion;
import cargadorProxy.repository.RepositoryFuentes;
import cargadorProxy.model.Fuente;
import cargadorProxy.model.TipoConexion;

public class RepositoryFuentesSeeder {

    private static RepositoryFuentesSeeder instance;
    private static RepositoryFuentes repositoryFuentes = RepositoryFuentes.getInstance();

    public static RepositoryFuentesSeeder getInstance() {
        if (instance == null) {
            synchronized (RepositoryFuentesSeeder.class) {
                if (instance == null) {
                    instance = new RepositoryFuentesSeeder();
                }
            }
        }
        return instance;
    }

    StrategyTipoConexion strategyAPIREST = new StrategyAPIREST();
    TipoConexion strategyAPIRESTENUM = TipoConexion.APIREST;

    Fuente fuente = new Fuente(
            1,
            "API de ejemplo",
            "https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",
            strategyAPIREST,
            "hola"
    );

    public void cargarRepos() {
        //repositoryFuentes.save(fuente);
    }
}
