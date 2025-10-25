package core.api.DTO;

import core.models.entities.hecho.Hecho;

public class HechoResumenDTO {
    public Integer id;
    public String nombre;
    public String descripcion;
    public String contribuyente;

    public HechoResumenDTO(Integer id, String titulo, String descripcion, String nombreContribuyente) {
    this.id = id;
    this.nombre = titulo;
    this.descripcion = descripcion;
    this.contribuyente = nombreContribuyente;
    }

    public static HechoResumenDTO from(Hecho h) {
        String nombreContribuyente = (h.getContribuyente() != null)
                ? h.getContribuyente().getNombre()
                : null;
        return new HechoResumenDTO(
                h.getId(),
                h.getTitulo(),
                h.getDescripcion(),
                nombreContribuyente
        );
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }
}
