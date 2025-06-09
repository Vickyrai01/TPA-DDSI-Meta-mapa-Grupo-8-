package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class EtiquetaCategoria extends Etiqueta {

    public EtiquetaCategoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;

    @Override
    public void cambiarCategoria (String categoriaNueva){
        this.setNombre(categoriaNueva);
        }
    }
