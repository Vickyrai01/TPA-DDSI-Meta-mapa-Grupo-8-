package servicioEstadisticas.model;
import servicioEstadisticas.repository.RepositoryServicioEstadisticas;
public class ServicioEstadisticas {

    private String provinciaConMasHechos;
    private String categoriaMasReportada;
    private Integer cantSolicitudesEliminacion = 200;

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

    public String getCategoriaMasReportada() {
        this.categoriaMasReportada = RepositoryServicioEstadisticas.categoriaMasReportada(); // borrar despues
        return categoriaMasReportada;
    }

    public String getProvinciaConMasHechos() {
        this.provinciaConMasHechos = RepositoryServicioEstadisticas.provinciaConMasHechos(); // borrar despues
        return provinciaConMasHechos;
    }

    public Integer getCantSolicitudesEliminacion() {
        this.cantSolicitudesEliminacion = RepositoryServicioEstadisticas.cantidadSolicitudesEliminacion();
        return cantSolicitudesEliminacion;
    }

    public String horarioxCategoria(String categoria){

        return RepositoryServicioEstadisticas.horarioxCategoria(categoria);

    }

    public String provicniaConMasHechosEnCategoria(String categoria ){

        return RepositoryServicioEstadisticas.provicniaConMasHechosEnCategoria(categoria);

    }

}
