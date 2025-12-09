package core.observabilidad;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class RegistroMetricas {

    private static final AtomicLong peticionesTotales = new AtomicLong(0);
    private static final AtomicLong erroresTotales   = new AtomicLong(0);
    private static final AtomicLong hechosCreadosTotales = new AtomicLong(0);
    private static final AtomicLong tiempoMsPeticionesTotal = new AtomicLong(0);

    private RegistroMetricas() {
    }

    public static void sumPeticiones() {
        peticionesTotales.incrementAndGet();
    }

    public static void sumErrores() {
        erroresTotales.incrementAndGet();
    }

    public static void sumHechosCreados() {
        hechosCreadosTotales.incrementAndGet();
    }

    public static void addHechosCreados(long cantidad) {
        if (cantidad <= 0) return;
        hechosCreadosTotales.addAndGet(cantidad);
    }


    public static void sumTiempoPeticion(long msegundos) {
        if (msegundos >= 0) {
            tiempoMsPeticionesTotal.addAndGet(msegundos);
        }
    }

    public static Map<String, Object> snapshot() {
        Map<String, Object> m = new HashMap<>();

        long reqs = peticionesTotales.get();
        long totalTime = tiempoMsPeticionesTotal.get();
        long avg = (reqs > 0) ? (totalTime / reqs) : 0;

        m.put("totalRequests", reqs);
        m.put("totalErrors", erroresTotales.get());
        m.put("totalHechosCreados", hechosCreadosTotales.get());
        m.put("totalRequestTimeMs", totalTime);
        m.put("avgResponseTimeMs", avg);

        return m;
    }
}
