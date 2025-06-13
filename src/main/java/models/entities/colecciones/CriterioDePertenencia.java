package models.entities.colecciones;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.TipoFuente;
import models.entities.hecho.Coordenadas;
import models.entities.hecho.Etiqueta;

import java.time.LocalDate;
import java.util.List;
@AllArgsConstructor
public class CriterioDePertenencia {

    private String titulo;

    private String descripcionDelHecho;

    private List<Etiqueta> etiquetas;

    private TipoFuente fuenteDeOrigen;

    private LocalDate fechaCarga;

    private LocalDate ultimaFechaModificacion;

    private String categoria;

    private Coordenadas ubicacion;

    private LocalDate fechaDesde;

    private LocalDate fechaHasta;

    private String descripcionDelCriterio;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionDelHecho() {
        return descripcionDelHecho;
    }

    public void setDescripcionDelHecho(String descripcionDelHecho) {
        this.descripcionDelHecho = descripcionDelHecho;
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

    public LocalDate getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }

    public void setUltimaFechaModificacion(LocalDate ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
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

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getDescripcionDelCriterio() {
        return descripcionDelCriterio;
    }

    public void setDescripcionDelCriterio(String descripcionDelCriterio) {
        this.descripcionDelCriterio = descripcionDelCriterio;
    }
}
