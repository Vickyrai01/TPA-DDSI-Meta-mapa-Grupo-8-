package models.agregador;

public class DetectorDeSpam {

    static boolean esSpam(String solicitud) {return false;}

    private static volatile DetectorDeSpam instance;

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
