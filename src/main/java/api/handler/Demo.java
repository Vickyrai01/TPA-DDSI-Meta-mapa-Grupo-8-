package api.handler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Demo {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean enEjecucion = false;

    public Demo(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    public void iniciarScheduler(){
        if(enEjecucion){
            System.out.println("El scheduler ya está en ejecución");
            return;
        }

        enEjecucion = true;
        System.out.println("Iniciando scheduler...");

        scheduler.scheduleAtFixedRate(this::verificarNuevosHechos,0,5, TimeUnit.SECONDS);
    }

    private void verificarNuevosHechos() {
         System.out.println("hola");
    }

    public void detenerScheduler() {
        enEjecucion = false;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Scheduler detenido");
    }

}
