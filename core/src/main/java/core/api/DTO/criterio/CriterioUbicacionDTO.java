package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioUbicacion;

public class CriterioUbicacionDTO extends CriterioDTO {
    private String provincia;

    public CriterioUbicacionDTO() {}

    public CriterioUbicacionDTO(Integer id, String provincia) {
        super(id);
        this.provincia = provincia;
    }

    @Override
    public CriterioUbicacion toEntity(){
        if (provincia == null || provincia.isEmpty()) {
            throw new IllegalArgumentException("provincia nula en CriterioUbicacionDTO");
        }
        return new CriterioUbicacion(provincia);
    }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
}
