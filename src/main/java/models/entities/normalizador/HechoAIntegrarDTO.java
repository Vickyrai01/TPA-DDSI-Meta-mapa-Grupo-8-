package models.entities.normalizador;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoAIntegrarDTO {

    public String titulo;
    public String descripcion;
    public String categoria;
    public String latitud;
    public String longitud;
    public String fechaDeHecho;
    public List<String> etiquetas;
    public String contribuyente;
    public List<String> multimedia; //A CHEQUEAR !!!!

    public HechoAIntegrarDTO() {}

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaDeHecho)
    {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaDeHecho = fechaDeHecho;
        this.etiquetas = null;
        this.contribuyente = null;
        this.multimedia = null;
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaDeHecho,
                             List<String> etiquetas, String contribuyente, List<String> multimedia)
    {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaDeHecho = fechaDeHecho;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.multimedia = multimedia;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaDeHecho() {
        return fechaDeHecho;
    }

    public void setFechaDeHecho(String fechaDeHecho) {
        this.fechaDeHecho = fechaDeHecho;
    }

    public Boolean tieneMismoTitulo(String tituloExterno){
        String tituloPropioLimpio = this.getTitulo().toLowerCase().replace(" ","");
        String tituloExternoLimpio = tituloExterno.toLowerCase().replace(" ","");
        //Los pone en minusculas y le elimina los espacios
        return tituloPropioLimpio.equals(tituloExternoLimpio);
    }
    @Override
    public String toString() {
        return "HechoAIntegrarDTO{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", fechaDeHecho='" + fechaDeHecho + '\'' +
                ", etiquetas=" + etiquetas +
                ", contribuyente='" + contribuyente + '\'' +
                ", multimedia=" + multimedia +
                '}';
    }

}
