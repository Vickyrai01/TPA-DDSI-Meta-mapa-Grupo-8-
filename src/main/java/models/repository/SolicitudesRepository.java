package models.repository;

import models.entities.hecho.Hecho;
import models.entities.solicitud.SolicitudDeEliminacion;

import java.util.List;

public class SolicitudesRepository {

    private List<SolicitudDeEliminacion> solicitudes;

    public void addSolicitudes(SolicitudDeEliminacion s){
        solicitudes.add(s);
    }

  public List<SolicitudDeEliminacion> getSolicitudes(){
        return solicitudes;
  }

    public void deleteSolicitudes(SolicitudDeEliminacion s){
        solicitudes.remove(s);
    }

}
