package application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HechoDTO(
    String hash,
    String nombre,
    String descripcion,
    String contribuyente,
    LocalDate fechaCarga,
    LocalDate fechaSuceso,
    LocalTime horaSuceso,
    List<String> multimedia,
    List<String> etiquetas,
    String latitud,
    String longitud,
    List<String> categorias,
    String estado





) {
    @Override
    public String hash() {
        return hash;
    }

    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public String descripcion() {
        return descripcion;
    }

    @Override
    public String contribuyente() {
        return contribuyente;
    }

    @Override
    public LocalDate fechaCarga() {
        return fechaCarga;
    }

    @Override
    public LocalDate fechaSuceso() {
        return fechaSuceso;
    }

    @Override
    public LocalTime horaSuceso() {
        return horaSuceso;
    }

    @Override
    public List<String> multimedia() {
        return multimedia;
    }

    @Override
    public List<String> etiquetas() {
        return etiquetas;
    }

    @Override
    public String latitud() {
        return latitud;
    }

    @Override
    public String longitud() {
        return longitud;
    }

    @Override
    public List<String> categorias() {
        return categorias;
    }

    @Override
    public String estado() {
        return estado;
    }
}
