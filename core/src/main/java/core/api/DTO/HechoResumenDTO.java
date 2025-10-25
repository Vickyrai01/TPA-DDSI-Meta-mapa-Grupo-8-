package core.api.DTO;

import core.models.entities.hecho.Hecho;

public class HechoResumenDTO {
    public String hash;
    public String nombre;
    public String descripcion;
    public String contribuyente;

    public HechoResumenDTO(String hash, String titulo, String descripcion, String nombreContribuyente) {
    this.hash = hash;
    this.nombre = titulo;
    this.descripcion = descripcion;
    this.contribuyente = nombreContribuyente;
    }

    public static HechoResumenDTO from(Hecho h) {
        String nombreContribuyente = (h.getContribuyente() != null)
                ? h.getContribuyente().getNombreCompleto()
                : null;
        return new HechoResumenDTO(
                h.getHash(),
                h.getTitulo(),
                h.getDescripcion(),
                nombreContribuyente
        );
    }

    public String getHash() { return hash; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getContribuyente() { return contribuyente; }

    public void setHash(String hash) { this.hash = hash; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setContribuyente(String contribuyente) { this.contribuyente = contribuyente; }
}
