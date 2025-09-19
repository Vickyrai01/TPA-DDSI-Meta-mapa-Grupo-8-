package cargadorProxy;


import cargadorEstatica.repository.RepositoryFuentes;
import cargadorEstatica.model.Fuente;
import core.models.entities.fuentes.TipoConexion;

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


    TipoConexion strategyAPIREST = TipoConexion.APIREST;

    Fuente fuente = new Fuente(
            1,
            "API de ejemplo",
            "https://684b1942165d05c5d35b843b.mockapi.io/metamapa/hechos",
            strategyAPIREST,
            "hola"
    );

    public void cargarRepos() {
    repositoryFuentes.agregarFuente(fuente);}
}
