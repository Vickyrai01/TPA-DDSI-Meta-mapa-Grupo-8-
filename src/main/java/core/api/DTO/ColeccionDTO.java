package core.api.DTO;

import core.models.entities.colecciones.criterios.Criterio;

import java.util.List;

public class ColeccionDTO {

    public ColeccionDTO(){}

    public ColeccionDTO(int id, String titulo, String descripcionColeccion, List<Criterio> listaCriterio, List<Integer> fuentes, List<Integer> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = listaCriterio;
        this.fuente = fuentes;
        this.hechos = hechos;
        this.identificadorHandle = identificadorHandle;
    }

    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String titulo;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String descripcionColeccion;
    public String getDescripcionColeccion() {
        return descripcionColeccion;
    }
    public void setDescripcionColeccion(String descripcionColeccion) {
        this.descripcionColeccion = descripcionColeccion;
    }

    private List<Criterio> criterioDePertenencia;
    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }
    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    private List<Integer> hechos;
    public List<Integer> getHechos() {
        return hechos;
    }
    public void setHechos(List<Integer> hechos) {
        this.hechos = hechos;
    }

    private String identificadorHandle;
    public String getIdentificadorHandle() {
        return identificadorHandle;
    }
    public void setIdentificadorHandle(String identificadorHandle) {
        this.identificadorHandle = identificadorHandle;
    }

    private List<Integer> fuente;
    public List<Integer> getFuente() {
        return fuente;
    }
    public void setFuente(List<Integer> fuente) {
        this.fuente = fuente;
    }

}
