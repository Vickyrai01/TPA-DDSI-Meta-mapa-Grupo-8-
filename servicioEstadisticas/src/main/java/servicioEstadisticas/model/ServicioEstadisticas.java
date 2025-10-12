package servicioEstadisticas.model;
import servicioEstadisticas.repository.RepositoryServicioEstadisticas;

import java.util.List;
import java.util.Map;

public class ServicioEstadisticas {

    private List<Map<String, Object>> provinciaConMasHechos;
    private List<Map<String, Object>> categoriaMasReportada;
    private Map<String, Object> cantSolicitudesEliminacion;

    private static volatile ServicioEstadisticas instance;

    public static ServicioEstadisticas getInstance() {
        if (instance == null) {
            synchronized (ServicioEstadisticas.class) {
                if (instance == null) {
                    instance = new ServicioEstadisticas();
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

    public List<Map<String, Object>> getCategoriaMasReportada() {
        return categoriaMasReportada;
    }


    public List<Map<String, Object>> getProvinciaConMasHechos() {
        return provinciaConMasHechos;
    }


    public Map<String, Object> getCantSolicitudesEliminacion() {
        return cantSolicitudesEliminacion;
    }

    public List<Map<String, Object>> horarioxCategoria(String categoria) {
        return RepositoryServicioEstadisticas.horarioxCategoria(categoria);
    }

    public List<Map<String, Object>> provinciaConMasHechosEnCategoria(String categoria) {
        return RepositoryServicioEstadisticas.provinciaConMasHechosEnCategoria(categoria);
    }


}
