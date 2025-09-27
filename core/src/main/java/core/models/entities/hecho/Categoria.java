package core.models.entities.hecho;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "categoria")
public class Categoria {

    @Id
    @Column(name = "id_categoria")
    private Integer id;

    @Column(name = "nombre")
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
