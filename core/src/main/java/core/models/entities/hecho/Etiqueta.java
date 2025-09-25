package core.models.entities.hecho;

public class Etiqueta {
    private String nombre;

    public Etiqueta(String categoria) {
        this.nombre = categoria;
    }
    public String getTipo() {
        return nombre;
    }
    public void setTipo(String tipo) {
        this.nombre = tipo;
    }
}
