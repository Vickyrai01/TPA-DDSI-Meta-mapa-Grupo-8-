package core.models.entities.hecho;


import javax.persistence.*;

@Entity(name = "etiqueta")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etiqueta")
    private Integer id;


    @Column(name = "nombre")
    private String nombre;

    public  Etiqueta(){}

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
