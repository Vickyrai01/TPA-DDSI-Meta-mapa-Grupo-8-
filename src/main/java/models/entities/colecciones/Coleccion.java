package models.entities.colecciones;

import lombok.AllArgsConstructor;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
public class Coleccion {

    public Coleccion(int id, String titulo, String descripcionColeccion, CriterioDePertenencia criterioDePertenencia, List<Fuente> fuentes, List<Hecho> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia;
        this.fuente =  new ArrayList<>(fuentes);;
        this.hechos = hechos;
        this.identificadorHandle = identificadorHandle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String titulo;

    private String descripcionColeccion;

    public List<Fuente> getFuente() {
        return fuente;
    }

    public void setFuente(List<Fuente> fuente) {
        this.fuente = fuente;
    }

    public void agregarFuente(Fuente f)
    {this.fuente.add(f);}

    public void eliminarFuente(Fuente f)
    {this.fuente.remove(f);}

    private List<Fuente> fuente;

    private CriterioDePertenencia criterioDePertenencia;

    private List<Hecho> hechos;

    private String identificadorHandle;

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

    public CriterioDePertenencia getCriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDePertenencia(CriterioDePertenencia criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public List<Hecho> getHechos() {
        return hechos;
    }

    public void setHechos(List<Hecho> hechos) {
        this.hechos = hechos;
    }

    public String getIdentificadorHandle() {
        return identificadorHandle;
    }

    public void setIdentificadorHandle(String identificadorHandle) {
        this.identificadorHandle = identificadorHandle;
    }

    public boolean yaEstaEsteHecho(Hecho hecho) {

        /// le pregunte a chati y me recomendo parallelstream() en vez de stream, la unica diferencia es que puede trabajar en paralelo, no se cual es mejor en este caso
        /// PD: le acabo de preguntar cual es mejor y me dijo que el paralelo es mejor cuando tengamos muchos hechos (asi que supongo que elgimos nostros cual agarrar)
        return hechos.stream().anyMatch(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));
        /// creo que deberia agregar un metodo en el hecho para poder hacer esto(me refiero al equals)
    }

    public void agregarHechosDeFuente(CriterioDePertenencia criterio){
        //this.fuente.extraerHechos(criterio).addAll(hechos); arreglar!! Porq ahora tenemos una lista de fuente.
    }

}
