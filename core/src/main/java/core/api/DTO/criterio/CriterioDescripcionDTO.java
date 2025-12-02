package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.CriterioDescripcion;

public class CriterioDescripcionDTO extends CriterioDTO {
    private String palabraClave;

    public CriterioDescripcionDTO() {}

    public CriterioDescripcionDTO(Integer id, String descripcion){
        super(id);
        this.palabraClave = descripcion;
    }

    @Override
    public CriterioDescripcion toEntity(){
        return new CriterioDescripcion(palabraClave);
    }

    public String getPalabraClave() {return palabraClave;}
    public void setPalabraClave(String clave) {this.palabraClave = clave; }
}
