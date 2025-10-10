package servicioEstadisticas.model;

import servicioEstadisticas.repository.RepositoryServicioEstadisticas;


public class ServicioEstadisticas {

    private String provinciaConMasHechos;
    private String categoriaMasReportada;
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






    //String categoriaProvincia, String categoriaDia

}
