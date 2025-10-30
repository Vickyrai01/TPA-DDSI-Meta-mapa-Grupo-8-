package core.api.DTO;

import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class HechoResumenDTO {
    public String hash;
    public String nombre;
    public String descripcion;
    public String contribuyente;
    public LocalDate fechaCarga;
    public LocalDate fechaSuceso;
    public LocalTime horaSuceso;
    public List<String> multimedia;
    public List<Etiqueta> etiquetas;
    public String latitud;
    public String longitud;


    public HechoResumenDTO(String hash, String titulo, String descripcion, String nombreContribuyente, String latitud, String longitud){
    this.hash = hash;
    this.nombre = titulo;
    this.descripcion = descripcion;
    this.contribuyente = nombreContribuyente;
    this.latitud = latitud;
    this.longitud = longitud;
    }

    public HechoResumenDTO(String hash, String nombre, String descripcion, String contribuyente, LocalDate fechaSuceso, LocalTime horaSuceso, List<String> multimedia, List<Etiqueta> etiquetas, String latitud, String longitud) {
        this.hash = hash;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contribuyente = contribuyente;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.multimedia = multimedia;
        this.etiquetas = etiquetas;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public static HechoResumenDTO from(Hecho h) {

        String nombreContribuyente = (h.getContribuyente() != null)
                ? h.getContribuyente().getNombreCompleto()
                : null;

        return new HechoResumenDTO(
                h.getHash(),
                h.getTitulo(),
                h.getDescripcion(),
                nombreContribuyente,
                h.getUbicacion().getLatitud().toString(),
                h.getUbicacion().getLongitud().toString()
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

    public LocalDate getFechaSuceso() {
        return fechaSuceso;
    }

    public void setFechaSuceso(LocalDate fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    public LocalTime getHoraSuceso() {
        return horaSuceso;
    }

    public void setHoraSuceso(LocalTime horaSuceso) {
        this.horaSuceso = horaSuceso;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getLongitud() {return longitud; }

    public String getLatitud() { return latitud;}

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
