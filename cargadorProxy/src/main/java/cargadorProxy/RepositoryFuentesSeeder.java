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

    Fuente fuente = new Fuente(3,"API Choque", "https://choques.com",  strategyAPIREST, "API REST");

    public void cargarRepos() {
        repositoryFuentes.save(fuente);
    }
}
