package core.api.DTO;

import core.api.DTO.criterio.CriterioDTO;

import java.util.List;

public class ColeccionConTodoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private List<FuenteDTO> fuentes;
    private List<HechoResumenDTO> hechos;
    private List<HechoResumenDTO> hechosVisibles;
    private List<CriterioDTO> criterios;

    public ColeccionConTodoDTO(Integer id, String titulo, String descripcion,
                               List<FuenteDTO> fuentes,
                               List<HechoResumenDTO> hechos,
                               List<HechoResumenDTO> hechosVisibles,
                               List<CriterioDTO> criterios) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.hechos = hechos;
        this.hechosVisibles = hechosVisibles;
        this.criterios = criterios;
    }

    // --- Getters y setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<FuenteDTO> getFuentes() { return fuentes; }
    public void setFuentes(List<FuenteDTO> fuentes) { this.fuentes = fuentes; }

    public List<HechoResumenDTO> getHechos() { return hechos; }
    public void setHechos(List<HechoResumenDTO> hechos) { this.hechos = hechos; }

    public List<HechoResumenDTO> getHechosVisibles() { return hechosVisibles; }
    public void setHechosVisibles(List<HechoResumenDTO> hechosVisibles) { this.hechosVisibles = hechosVisibles; }

    public List<CriterioDTO> getCriterios() { return criterios; }
    public void setCriterios(List<CriterioDTO> criterios) { this.criterios = criterios; }
}
