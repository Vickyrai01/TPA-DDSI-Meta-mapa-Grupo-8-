package api.DTO;

import models.entities.colecciones.criterios.Criterio;

import java.util.List;

public class ColeccionResponse {

    public ColeccionResponse(){}

    public ColeccionResponse(int id, String titulo, String descripcionColeccion, List<Criterio> listaCriterio, List<Integer> fuente, List<Integer> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = listaCriterio;
        this.fuente = fuente;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionColeccion() {
        return descripcionColeccion;
    }

    public void setDescripcionColeccion(String descripcionColeccion) {
        this.descripcionColeccion = descripcionColeccion;
    }


    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public List<Integer> getHechos() {
        return hechos;
    }

    public void setHechos(List<Integer> hechos) {
        this.hechos = hechos;
    }

    public String getIdentificadorHandle() {
        return identificadorHandle;
    }

    public void setIdentificadorHandle(String identificadorHandle) {
        this.identificadorHandle = identificadorHandle;
    }

    private String titulo;

    private String descripcionColeccion;

    public List<Integer> getFuente() {
        return fuente;
    }

    public void setFuente(List<Integer> fuente) {
        this.fuente = fuente;
    }

    private List<Integer> fuente;

    private List<Criterio> criterioDePertenencia;

    private List<Integer> hechos;

    private String identificadorHandle;
}
