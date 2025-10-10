package servicioEstadisticas.model;

import servicioEstadisticas.repository.RepositoryServicioEstadisticas;


public class ServicioEstadisticas {

    private String provinciaConMasHechos = "Buenos Aires";
    private String categoriaMasReportada = "ASESINARON A VRAI";
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
        return categoriaMasReportada;
    }

    public String getProvinciaConMasHechos() {
        return provinciaConMasHechos;
    }

    public Integer getCantSolicitudesEliminacion() {
        return cantSolicitudesEliminacion;
    }



    public String horarioxCategoria(String categoria){

        return "jiji jija";

    }

    public String provicniaConMasHechosEnCategoria(String categoria ){

        return "jiji jija";

    }

    //String categoriaProvincia, String categoriaDia

}
