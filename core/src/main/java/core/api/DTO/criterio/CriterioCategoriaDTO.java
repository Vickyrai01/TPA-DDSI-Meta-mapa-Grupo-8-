package core.api.DTO.criterio;

public class CriterioCategoriaDTO extends CriterioDTO {
    private String categoria;

    public CriterioCategoriaDTO(Integer id, String categoria) {
        super(id);
        this.categoria = categoria;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
