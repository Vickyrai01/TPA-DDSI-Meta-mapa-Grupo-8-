package servicioEstadisticas;
import servicioEstadisticas.model.repository.RepositoryServicioEstadisticas;

import java.util.List;
import java.util.Map;

public class GeneradorTodasEstadisticas {

    private List<Map<String, Object>> provinciaConMasHechos;
    private List<Map<String, Object>> categoriaMasReportada;
    private Map<String, Object> cantSolicitudesEliminacion;

    public static volatile GeneradorTodasEstadisticas instance;

    public static GeneradorTodasEstadisticas getInstance() {
        if (instance == null) {
            synchronized (GeneradorTodasEstadisticas.class) {
                if (instance == null) {
                    instance = new GeneradorTodasEstadisticas();
                }
            }
        }
        return instance;
    }

    public void actualizarEstadisticas(){
        this.provinciaConMasHechos = RepositoryServicioEstadisticas.provinciaConMasHechos();
        this.categoriaMasReportada = RepositoryServicioEstadisticas.categoriaMasReportada();
        this.cantSolicitudesEliminacion = RepositoryServicioEstadisticas.cantidadSolicitudesEliminacion();
    }

    public List<Map<String, Object>> getProvinciaConMasHechos() {return provinciaConMasHechos;}

     public List<Map<String, Object>> getCategoriaMasReportada() { return categoriaMasReportada;}

    public Map<String, Object> getCantSolicitudesEliminacion() {return cantSolicitudesEliminacion;}


    public List<Map<String, Object>> horarioxCategoria(String categoria) {
        return RepositoryServicioEstadisticas.horarioxCategoria(categoria);
    }

    public List<Map<String, Object>> provinciaConMasHechosEnCategoria(String categoria) {
        return RepositoryServicioEstadisticas.provinciaConMasHechosEnCategoria(categoria);
    }
}