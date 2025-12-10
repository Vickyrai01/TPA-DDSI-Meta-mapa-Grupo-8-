package core.models.entities.usuario;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String rol; // Ejemplo: "ADMIN" o "USER"

    @Column(nullable = true)
    private String foto;

    @Column()
    private String contrasena;

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // Constructor vacío requerido por JPA
    public Usuario() {}

    // Constructor útil
    public Usuario(String nombre, String correo, String rol, String foto, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.foto = foto;
        this.contrasena = contrasena;
    }
}
