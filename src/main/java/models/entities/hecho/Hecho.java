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

    public Hecho(Integer id, Coordenadas ubicacion, Categoria categoria,
                 List<SugerenciaDeCambio> sugerenciaDeCambio,
                 LocalDate ultimaFechaModificacion, List<String> multimedia,
                 Estado estado, Contribuyente contribuyente,
                 LocalDate fechaCarga, LocalDate fechaSuceso,
                 TipoFuente fuenteDeOrigen,
                 List<Etiqueta> etiquetas, String descripcion, String titulo,String codigoDeFuente) {
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
        this.codigoDeFuente = codigoDeFuente;
    }

    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    private String titulo;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private List<Etiqueta> etiquetas;
    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }
    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    private TipoFuente fuenteDeOrigen;
    public TipoFuente getFuenteDeOrigen() {
        return fuenteDeOrigen;
    }
    public void setFuenteDeOrigen(TipoFuente fuenteDeOrigen) {
        this.fuenteDeOrigen = fuenteDeOrigen;
    }

    private LocalDate fechaCarga;
    public LocalDate getFechaCarga() {
        return fechaCarga;
    }
    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    private LocalDate fechaSuceso;
    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }
    public void setFechaSuceso(LocalDate fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    private Contribuyente contribuyente;
    public Contribuyente getContribuyente() {
        return contribuyente;
    }
    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    private Estado estado;
    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    private List<String> multimedia;
    public List<String> getMultimedia() {
        return multimedia;
    }
    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    private LocalDate ultimaFechaModificacion;
    public LocalDate getUltimaFechaModificacion() {
        return ultimaFechaModificacion;
    }
    public void setUltimaFechaModificacion(LocalDate ultimaFechaModificacion) {
        this.ultimaFechaModificacion = ultimaFechaModificacion;
    }

    private List<SugerenciaDeCambio> sugerenciaDeCambio;
    public List<SugerenciaDeCambio> getSugerenciaDeCambio() {
        return sugerenciaDeCambio;
    }
    public void setSugerenciaDeCambio(List<SugerenciaDeCambio> sugerenciaDeCambio) {
        this.sugerenciaDeCambio = sugerenciaDeCambio;
    }

    private Categoria categoria;
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    private Coordenadas ubicacion;
    public Coordenadas getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(Coordenadas ubicacion) {
        this.ubicacion = ubicacion;
    }

    private String codigoDeFuente;
    public String getCodigoDeFuente() {
        return codigoDeFuente;
    }

    public Hecho(){}

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


    public boolean perteneceAFuente(List<String> listaFuentes){ //Recibe los IDs de las fuentes
        return listaFuentes.contains(this.codigoDeFuente);
    }


}
