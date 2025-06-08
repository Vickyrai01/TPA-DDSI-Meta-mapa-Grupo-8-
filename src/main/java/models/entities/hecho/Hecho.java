package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class Hecho {

    @Setter
    @Getter
    private String titulo;

    @Setter
    @Getter
    private String descripcion;

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
    private Estado estado;

    @Setter
    @Getter
    private List<String> multimedia;

    @Setter
    @Getter
    private LocalDateTime ultimaFechaModificacion;

    @Setter
    @Getter
    private List<SugerenciaDeCambio> sugerenciaDeCambio;


    public Hecho(String titulo, String descripcion, LocalDate fecha) {
        this.titulo= titulo;
        this.descripcion = descripcion;
        this.fechaCarga = LocalDate.from(fecha);
    }

    @Override
    public String toString() {
        return "Titulo: " + titulo + " - Fecha: " + fechaCarga ;
    }
}
