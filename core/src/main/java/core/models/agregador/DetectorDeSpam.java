package core.models.agregador;

public class DetectorDeSpam {

    private static volatile DetectorDeSpam instance;

    public static boolean esSpam(String solicitud) {return false;}



    public DetectorDeSpam() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static DetectorDeSpam getInstance() {
        if (instance == null) {
            synchronized (DetectorDeSpam.class) {
                if (instance == null) {
                    instance = new DetectorDeSpam();
                }
            }
        }
        return instance;
    }
}
