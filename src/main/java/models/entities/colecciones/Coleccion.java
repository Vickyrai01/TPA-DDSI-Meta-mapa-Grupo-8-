package models.entities.colecciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.List;
@AllArgsConstructor
public class Coleccion {

    private String titulo;

    private String descripcionColeccion;

    private Fuente fuente;

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
        this.fuente.extraerHechos(criterio).addAll(hechos);
    }

}
