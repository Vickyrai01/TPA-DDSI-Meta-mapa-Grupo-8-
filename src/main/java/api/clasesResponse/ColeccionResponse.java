package api.clasesResponse;

import models.entities.colecciones.CriterioDePertenencia;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.List;

public class ColeccionResponse {

    public ColeccionResponse(){}

    public ColeccionResponse(int id, String titulo, String descripcionColeccion, CriterioDePertenencia criterioDePertenencia, Fuente fuente, List<Integer> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia;
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

    public Fuente getFuente() {
        return fuente;
    }

    public void setFuente(Fuente fuente) {
        this.fuente = fuente;
    }

    public CriterioDePertenencia getCriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDePertenencia(CriterioDePertenencia criterioDePertenencia) {
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

    private Fuente fuente;

    private CriterioDePertenencia criterioDePertenencia;

    private List<Integer> hechos;

    private String identificadorHandle;
}
