package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.util.List;


public class Hecho {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Fuente getFuenteDeOrigen() {
        return fuenteDeOrigen;
    }

    public void setFuenteDeOrigen(Fuente fuenteDeOrigen) {
        this.fuenteDeOrigen = fuenteDeOrigen;
    }

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public LocalDate getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }

    public void setUltimaFechaModificacion(LocalDate ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    public List<SugerenciaDeCambio> getSugerenciaDeCambio() {
        return sugerenciaDeCambio;
    }

    public void setSugerenciaDeCambio(List<SugerenciaDeCambio> sugerenciaDeCambio) {
        this.sugerenciaDeCambio = sugerenciaDeCambio;
    }

    private Integer id;


    private String titulo;

    private String descripcion;

    public Hecho() {

    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    private List<Etiqueta> etiquetas;

    private Fuente fuenteDeOrigen;

    private LocalDate fechaCarga;

    private Contribuyente contribuyente;

    public Estado estado;

    private List<String> multimedia;

    private LocalDate ultimaFechaModificacion;

    private List<SugerenciaDeCambio> sugerenciaDeCambio;

    @Override
    public String toString() {
        return "Titulo: " + titulo + " - Fecha: " + fechaCarga;
    }

    public Hecho(String titulo, String descripcion, List<Etiqueta> etiquetas,
                 Fuente fuenteDeOrigen, LocalDate fechaCarga, Contribuyente contribuyente, Estado estado,
                 List<String> multimedia, List<SugerenciaDeCambio> sugerenciaDeCambio, LocalDate ultimaFechaModificacion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.etiquetas = etiquetas;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.fechaCarga = fechaCarga;
        this.contribuyente = contribuyente;
        this.estado = estado;
        this.multimedia = multimedia;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    public void modificarEtiquetaLugar(EtiquetaLugar lugar, Double latitud, Double longitud){
        this.etiquetas.forEach(unaEtiqueta -> unaEtiqueta.cambiarUbicacion(latitud, longitud));
    }

    public void modificarEtiquetaCategoria(EtiquetaCategoria categoria, String categoriaNueva){
        this.etiquetas.forEach(unaEtiqueta -> unaEtiqueta.cambiarCategoria(categoriaNueva));
    }

    public void modificarEtiquetaFecha(EtiquetaFecha fecha, LocalDate fechaNueva){
        this.etiquetas.forEach(unaEtiqueta -> unaEtiqueta.cambiarFecha(fechaNueva));
    }

    public void agregarEtiqueta(Etiqueta etiqueta)
    { etiquetas.add(etiqueta);}
}
