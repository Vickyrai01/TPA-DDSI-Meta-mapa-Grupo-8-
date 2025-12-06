package cargadorEstatica.repository;

import cargadorEstatica.model.Fuente;
import cargadorEstatica.model.StrategyCSV;
import cargadorEstatica.model.StrategyTipoConexion;

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

    private StrategyTipoConexion StrategyCSV = new StrategyCSV();
    Fuente fuenteEjemplo = new Fuente(
            1,
            "Desastres Sanitarios",
            "desastres_sanitarios_contaminacion_argentina.csv",
            StrategyCSV,
            "CSV"
    );

    public void cargarRepos() {
        repositoryFuentes.save(fuenteEjemplo);
    }

}