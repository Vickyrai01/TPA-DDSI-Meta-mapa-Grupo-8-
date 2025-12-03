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

    StrategyTipoConexion StrategyCSV = new StrategyCSV();
    Fuente fuenteEjemplo = new Fuente(1,"Desastres Sanitarios", "1_desastres_sanitarios_contaminacion_argentina.csv", StrategyCSV, "CSV");
    Fuente fuentePrueba = new Fuente(2,"Fuente de prueba 1", "eventosSanitariosPrueba1.csv", StrategyCSV, "CSV");
    Fuente fuente2 = new Fuente(3,"Fuente 2", "eventosSanitariosPrueba2.csv", StrategyCSV, "CSV");

    public void cargarRepos() {
        //repositoryFuentes.save(fuenteEjemplo);
        //repositoryFuentes.save(fuentePrueba);
        //repositoryFuentes.save(fuente2);
    }

}