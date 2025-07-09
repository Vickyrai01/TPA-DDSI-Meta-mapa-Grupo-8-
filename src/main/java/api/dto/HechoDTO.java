package api.dto;

import java.time.LocalDate;

public class HechoDTO {
    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    private String titulo;
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    private String descripcion;
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    private LocalDate fechaSuceso;
    public LocalDate getFechaSuceso() {return fechaSuceso;}
    public void setFechaSuceso(LocalDate fechaSuceso) {this.fechaSuceso = fechaSuceso;}

    private Double latitud;
    public Double getLatitud() {return latitud;}
    public void setLatitud(Double latitud) {this.latitud = latitud;}

    private Double longitud;
    public Double getLongitud() {return longitud;}
    public void setLongitud(Double longitud) {this.longitud = longitud;}

}
