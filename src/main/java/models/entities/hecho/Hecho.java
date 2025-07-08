package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;
import java.util.List;



public class Hecho {

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

    private Categoria categoria;

    private Coordenadas ubicacion;

    public Hecho(Integer id, Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.categoria = categoria;
        this.sugerenciaDeCambio = sugerenciaDeCambio;
        this.ultimaFechaModificacion = ultimaFechaModificacion;
        this.multimedia = multimedia;
        this.estado = estado;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.fuenteDeOrigen = fuenteDeOrigen;
        this.etiquetas = etiquetas;
        this.descripcion = descripcion;
        this.titulo = titulo;
    }

    public Hecho(){}

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Coordenadas getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Coordenadas ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo +
                "\n ||| ID: " + id +
                "\n ||| Descripcion:" + descripcion +
                "\n ||| Fecha Suceso: " + fechaSuceso +
                "\n ||| Fecha Carga: " + fechaCarga +
                "\n ||| Sugerencia De Cambio: " + sugerenciaDeCambio +
                "\n ||| Coordenadas: " + ubicacion.toString();
    }

    public void agregarEtiqueta(Etiqueta etiqueta)
    { etiquetas.add(etiqueta);}

    public boolean pasoUnaSemana(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaActualMenosUnaSemana = fechaActual.minusDays(7);
        return fechaActualMenosUnaSemana.isBefore(fechaSuceso);
    }

    public void desactivarse(){
        this.setEstado(Estado.INACTIVO);
    }


}
