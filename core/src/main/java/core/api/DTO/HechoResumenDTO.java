package core.api.DTO;

import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
    public List<String> etiquetas;
    public String latitud;
    public String longitud;
    public List<String> categorias;
    public String estado;


    public HechoResumenDTO(String hash, String titulo, String descripcion, String nombreContribuyente, LocalDate fechaSuceso, LocalTime horaSuceso, List<String> multimedia, List<String> etiquetas, String latitud, String longitud, List<String> categorias, String estado) {
        this.hash = hash;
        this.nombre = titulo;
        this.descripcion = descripcion;
        this.contribuyente = nombreContribuyente;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.multimedia = multimedia;
        this.etiquetas = etiquetas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.categorias = categorias;
        this.estado = estado;
    }

    public HechoResumenDTO(String hash, String nombre, String descripcion, String contribuyente, LocalDate fechaSuceso, LocalTime horaSuceso, List<String> multimedia, String latitud, String longitud) {
        this.hash = hash;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contribuyente = contribuyente;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.multimedia = multimedia;
        //this.etiquetas = etiquetas;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public HechoResumenDTO(String hash, String nombre, String descripcion, String contribuyente, LocalDate fechaCarga, LocalDate fechaSuceso, LocalTime horaSuceso, List<String> multimedia, List<String> etiquetas, String latitud, String longitud, List<String> categorias, String estado) {
        this.hash = hash;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.multimedia = multimedia;
        this.etiquetas = etiquetas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.categorias = categorias;
        this.estado = estado;
    }

    public static HechoResumenDTO from(Hecho h) {

        String nombreContribuyente = (h.getContribuyente() != null)
                ? h.getContribuyente().getNombreCompleto()
                : null;

        List<String> etiquetas = (h.getEtiquetas() != null)
                ? h.getEtiquetas().stream()
                .map(Etiqueta::getNombre)
                .toList()
                : Collections.emptyList();

        String latitud = (h.getUbicacion() != null && h.getUbicacion().getLatitud() != null)
                ? h.getUbicacion().getLatitud().toString()
                : null;

        String longitud = (h.getUbicacion() != null && h.getUbicacion().getLongitud() != null)
                ? h.getUbicacion().getLongitud().toString()
                : null;

        List<String> categorias = (h.getCategoria() != null)
                ? List.of(h.getCategoria().toString()) // o .getNombre() si corresponde
                : List.of();

        // Lógica de multimedia agregada
        List<String> multimedia = (h.getMultimedia() != null)
                ? new ArrayList<>(h.getMultimedia())
                : Collections.emptyList();

        return new HechoResumenDTO(
                h.getHash(),
                h.getTitulo(),
                h.getDescripcion(),
                nombreContribuyente,
                h.getFechaCarga(),      // Se mantiene fechaCarga
                h.getFechaSuceso(),
                h.getHoraSuceso(),
                multimedia,             // Se pasa la lista en lugar de null
                etiquetas,
                latitud,
                longitud,
                categorias,
                h.getEstado().toString()
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

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
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

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}