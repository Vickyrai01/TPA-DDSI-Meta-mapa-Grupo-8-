package core.api.DTO.criterio;

public class CriterioNombreDTO extends CriterioDTO {
    private String palabraClave;

    public CriterioNombreDTO() {}

    public CriterioNombreDTO(Integer id, String descripcion) {
        super(id);
        this.palabraClave = descripcion;
    }

    public String getPalabraClave() {
        return palabraClave;
    }
    public void setPalabraClave(String clave) {
        this.palabraClave = palabraClave;
    }
}
