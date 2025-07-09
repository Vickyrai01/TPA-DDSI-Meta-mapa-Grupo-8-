package models.entities.colecciones;

import models.entities.colecciones.criterios.Criterio;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;
public class Coleccion {

    public Coleccion(int id, String titulo, String descripcionColeccion, List<Criterio> criterioDePertenencia, List<Fuente> fuentes, List<Hecho> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia;
        this.fuentes = fuentes;
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

    private List<Fuente> fuentes;

    public void agregarFuente (Fuente f)
    {fuentes.add(f);}

    public void agregarFuentes (List<Fuente> listaFuentes){
        fuentes.addAll(listaFuentes);
    }

    public void eliminarFuente (Fuente f)
    {fuentes.remove(f);}

    public List<Fuente> getFuentes() {
        return this.fuentes;
    }

    public void setFuentes(List<Fuente> fuentes){
        this.fuentes = fuentes;
    }

    private String descripcionColeccion;

    private List<Criterio> criterioDePertenencia;

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

    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {
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

    public void agregarCriterio(Criterio criterio)
    {criterioDePertenencia.add(criterio);};

    public void eliminarCriterio(Criterio criterio)
    {criterioDePertenencia.remove(criterio);};

    public void setIdentificadorHandle(String identificadorHandle) {
        this.identificadorHandle = identificadorHandle;
    }

    public List<String> extraerCodigosDeFuentes(List<Fuente> fuentes){
        return fuentes.stream().map(Fuente::getCodigoDeFuente).toList();
    }

    public void agregarHechosDeFuentes(List<Hecho> hechos){
       this.hechos.addAll(hechos.stream().filter(unHecho -> unHecho.perteneceAFuente(extraerCodigosDeFuentes(this.fuentes))).toList());
    }

    public void agregarHecho(Hecho hecho){
        {this.hechos.add(hecho);}
    }

}
