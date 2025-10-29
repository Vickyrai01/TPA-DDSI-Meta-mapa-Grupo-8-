package servicioEstadisticas.model;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SchedulerEstadisticas {

    private final GeneradorTodasEstadisticas generadorTodasEstadisticas;

    public SchedulerEstadisticas() {
        this.generadorTodasEstadisticas = GeneradorTodasEstadisticas.getInstance();
    }

    // Ejecutar cada 24 horas
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // milliseconds
    public void ejecutarActualizacionEstadisticas() {
        System.out.println("Iniciando actualización de estadísticas...");
        generadorTodasEstadisticas.actualizarEstadisticas();
        System.out.println("Actualización de estadísticas completada");
    }

}
