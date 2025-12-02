package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioNombre;

public class CriterioNombreDTO extends CriterioDTO {
    private String palabraClave;

    public CriterioNombreDTO() {}

    public CriterioNombreDTO(Integer id, String descripcion) {
        super(id);
        this.palabraClave = descripcion;
    }

    @Override
    public CriterioNombre toEntity(){
        return new CriterioNombre(palabraClave);
    }

    public String getPalabraClave() {
        return palabraClave;
    }
    public void setPalabraClave(String clave) {
        this.palabraClave = palabraClave;
    }
}
