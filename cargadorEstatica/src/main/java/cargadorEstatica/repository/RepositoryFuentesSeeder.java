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

    //Fuente fuente1 = new Fuente(2,"Accidentes en AMBA", "eventosSanitariosPrueba1.csv",  StrategyCSV, "CSV");
    Fuente fuente4 = new Fuente(3,"CSV Incendio", null,  StrategyCSV, "CSV");

    public void cargarRepos() {
        //repositoryFuentes.save(fuente1);
        repositoryFuentes.save(fuente4);
    }

}