package core.api.DTO.criterio;

import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioCategoria;
import core.models.entities.hecho.Categoria;

public class CriterioCategoriaDTO extends CriterioDTO {
    private String categoria;

    public CriterioCategoriaDTO() {}

    public CriterioCategoriaDTO(Integer id, String categoria) {
        super(id);
        this.categoria = categoria;
    }

    //@Override
    public Criterio toEntity(){
        Categoria categoriaReal = new Categoria(categoria);
        return new CriterioCategoria(categoriaReal);
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
