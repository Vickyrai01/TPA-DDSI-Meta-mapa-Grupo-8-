package models.entities.HechoAIntegrarDTO;

import models.entities.fuentes.TipoFuente;
import models.entities.hecho.*;

import java.time.LocalDate;
import java.util.List;

public class HechoAIntegrarDTO {


    public HechoAIntegrarDTO(String titulo,String descripcion, String categoria,
                             String longitud,String latitud , String fechaDeHecho){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.longitud = longitud;
        this.latitud = latitud;
        this.fechaDeHecho = fechaDeHecho;
    }

    private String titulo;
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    private String descripcion;
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    private String categoria;
    public String getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}

    private String latitud;
    public String getLatitud() {return latitud;}
    public void setLatitud(String latitud) {this.latitud = latitud;}

    private String longitud;
    public String getLongitud() {return longitud;}
    public void setLongitud(String longitud) {this.longitud = longitud;}

    private String fechaDeHecho;
    public String getFechaDeHecho() {return fechaDeHecho;}
    public void setFechaDeHecho(String fechaDeHecho) {this.fechaDeHecho = fechaDeHecho;}
}











