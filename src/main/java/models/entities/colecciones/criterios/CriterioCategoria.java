package models.entities.colecciones.criterios;

import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;

public class CriterioCategoria implements Criterio{
    private Categoria categoria;

    public CriterioCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //TENER EN CUENTA QUE TIENE QUE SER EL MISMO OBJETO EN ESPECIFICO!!
    //POR AHORA: ES CON COMPARACIÓN DE STRING :)
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        if (hecho.getCategoria() == null || hecho.getCategoria().getNombre() == null) {
            throw new IllegalStateException("El hecho tiene una categoría nula o sin nombre");
        }
        if (categoria == null || categoria.getNombre() == null) {
            throw new IllegalStateException("El criterio tiene una categoría nula o sin nombre");
        }

        return hecho.getCategoria().getNombre().toLowerCase().contains(categoria.getNombre().toLowerCase());

}}
