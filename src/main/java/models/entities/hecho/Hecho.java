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

    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String titulo;

    @Setter
    @Getter
    private String descripcion;

    public Hecho() {

    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    @Setter
    @Getter
    private List<Etiqueta> etiquetas;

    @Setter
    @Getter
    private Fuente fuenteDeOrigen;

    @Setter
    @Getter
    private LocalDate fechaCarga;

    @Setter
    @Getter
    private Contribuyente contribuyente;

    @Setter
    @Getter
    public Estado estado;

    @Setter
    @Getter
    private List<String> multimedia;

    @Setter
    @Getter
    private LocalDate ultimaFechaModificacion;

    @Setter
    @Getter
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
