package api.clasesResponse;

import java.time.LocalDate;

public class HechoResponse {
    private Integer id;

    private String titulo;

    private String descripcion;

    private LocalDate fechaCarga;

    private LocalDate fechaSuceso;

    private Double latitud;

    private Double longitud;

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }

    public void setFechaSuceso(LocalDate fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
