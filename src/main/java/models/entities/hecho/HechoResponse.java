package models.entities.hecho;

import models.entities.fuentes.TipoFuente;

import java.time.LocalDate;
import java.util.List;

public class HechoResponse {
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

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public TipoFuente getFuenteDeOrigen() {
        return fuenteDeOrigen;
    }

    public void setFuenteDeOrigen(TipoFuente fuenteDeOrigen) {
        this.fuenteDeOrigen = fuenteDeOrigen;
    }

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }

    public void setFechaSuceso(LocalDate fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    public LocalDate getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }

    public void setUltimaFechaModificacion(LocalDate ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public List<SugerenciaDeCambio> getSugerenciaDeCambio() {
        return sugerenciaDeCambio;
    }

    public void setSugerenciaDeCambio(List<SugerenciaDeCambio> sugerenciaDeCambio) {
        this.sugerenciaDeCambio = sugerenciaDeCambio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Coordenadas getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Coordenadas ubicacion) {
        this.ubicacion = ubicacion;
    }

    private Integer id;

    private String titulo;

    private String descripcion;

    private List<Etiqueta> etiquetas;

    private TipoFuente fuenteDeOrigen;

    private LocalDate fechaCarga;

    private LocalDate fechaSuceso;

    private Contribuyente contribuyente;

    private Estado estado;

    private List<String> multimedia;

    private LocalDate ultimaFechaModificacion;

    private List<SugerenciaDeCambio> sugerenciaDeCambio;

    private String categoria;

    private Coordenadas ubicacion;
}
