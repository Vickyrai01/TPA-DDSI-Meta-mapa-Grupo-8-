package servicioEstadisticas.model;
import servicioEstadisticas.repository.RepositoryServicioEstadisticas;

import java.util.List;
import java.util.Map;

public class ServicioEstadisticas {

    private List<String> provinciaConMasHechos;
    private List<Map<String, Object>> categoriaMasReportada;
    private Integer cantSolicitudesEliminacion;

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


    public List<String> getProvinciaConMasHechos() {
        return provinciaConMasHechos;
    }

    public Integer getCantSolicitudesEliminacion() {
        return cantSolicitudesEliminacion;
    }

    public String horarioxCategoria(String categoria){

        return RepositoryServicioEstadisticas.horarioxCategoria(categoria);

    }

    public String provicniaConMasHechosEnCategoria(String categoria ){

        return RepositoryServicioEstadisticas.provicniaConMasHechosEnCategoria(categoria);

    }

}
