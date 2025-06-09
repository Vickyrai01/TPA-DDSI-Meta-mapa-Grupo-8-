package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.entities.fuentes.Fuente;
import java.time.LocalDate;
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
    private LocalDate ultimaFechaModificacion;

    @Setter
    @Getter
    private List<SugerenciaDeCambio> sugerenciaDeCambio;

    public void modificarEtiquetaLugar(EtiquetaLugar lugar, Double latitud, Double longitud){
        this.getEtiquetas().forEach(unaEtiqueta -> unaEtiqueta.cambiarUbicacion(latitud, longitud));
    }

    public void modificarEtiquetaCategoria(EtiquetaCategoria categoria, String categoriaNueva){
        this.getEtiquetas().forEach(unaEtiqueta -> unaEtiqueta.cambiarCategoria(categoriaNueva));
    }

    public void modificarEtiquetaFecha(EtiquetaFecha fecha, LocalDate fechaNueva){
        this.getEtiquetas().forEach(unaEtiqueta -> unaEtiqueta.cambiarFecha(fechaNueva));
    }

    public void agregarEtiqueta(Etiqueta etiqueta)
    { etiquetas.add(etiqueta);}
}
