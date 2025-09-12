package cargadorDINAMICO;


public class RepositoryFuentesSeeder {

    private static RepositoryFuentesSeeder instance;

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
}
