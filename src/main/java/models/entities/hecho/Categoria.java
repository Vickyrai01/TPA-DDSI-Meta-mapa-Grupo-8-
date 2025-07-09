package models.entities.hecho;

import models.entities.colecciones.criterios.CriterioCategoria;

public class Categoria {

    String nombre;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(String categoria) {
        this.nombre = categoria;
    }
    public Categoria(){}
}
