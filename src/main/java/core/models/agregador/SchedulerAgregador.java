package core.models.agregador;
import core.models.agregador.cargadores.HandlerCargadores;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerAgregador {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean enEjecucion = false;
    private final ServicioDeAgregacion servicioDeAgregacion = ServicioDeAgregacion.getInstance();
    private final HandlerCargadores handlerCargadores = HandlerCargadores.getInstance();


    public SchedulerAgregador(boolean enEjecucion) {
        this.enEjecucion = enEjecucion;
    }

    public void iniciarScheduler(){
        if(enEjecucion){
            System.out.println("El scheduler ya está en ejecución");
            return;
        }

        enEjecucion = true;
        System.out.println("Iniciando scheduler...");

        scheduler.scheduleAtFixedRate(this::verificarNuevosHechos,0,30, TimeUnit.SECONDS);
    }

    private void verificarNuevosHechos() {
    /*
        servicioDeAgregacion.actualizarColecciones(handlerCargadores.extraerHechosAIntegrar());
    */
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
