package core.api.DTO.criterio;

public class CriterioUbicacionDTO extends CriterioDTO{
    private Double latitud;
    private Double longitud;

    public CriterioUbicacionDTO() {}

    public CriterioUbicacionDTO(Integer id, Double latitud, Double longitud) {
        super(id);
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}
