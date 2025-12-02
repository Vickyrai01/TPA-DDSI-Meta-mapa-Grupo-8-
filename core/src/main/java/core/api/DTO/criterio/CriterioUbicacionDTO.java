package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioUbicacion;
import core.models.entities.hecho.Coordenadas;

public class CriterioUbicacionDTO extends CriterioDTO{
    private Double latitud;
    private Double longitud;

    public CriterioUbicacionDTO() {}

    public CriterioUbicacionDTO(Integer id, Double latitud, Double longitud) {
        super(id);
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @Override
    public CriterioUbicacion toEntity(){
        Coordenadas coordenadas = new Coordenadas(latitud,longitud);
        return new CriterioUbicacion(coordenadas);
    }

    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}
