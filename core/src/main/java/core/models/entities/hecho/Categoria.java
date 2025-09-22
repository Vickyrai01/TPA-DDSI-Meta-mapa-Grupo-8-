package core.models.entities.hecho;

public class Categoria {

    int id;
    String nombre;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Categoria(String categoria) {
        this.nombre = categoria;
    }
    public Categoria(){}
}
