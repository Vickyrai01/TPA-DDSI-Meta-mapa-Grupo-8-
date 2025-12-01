package servicioEstadisticas.model.entities;

import javax.persistence.*;


@Entity
@Table(name = "solicitud_de_eliminacion")
public class SolicitudSpam {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_spam;

    @Column(name = "aceptada")
    private Boolean aceptada;


    public SolicitudSpam(Boolean aceptada) {this.aceptada = aceptada;}

    public SolicitudSpam() {}


    public Integer getId_spam() {
        return id_spam;
    }

    public void setId_spam(Integer id_spam) {
        this.id_spam = id_spam;
    }

    public Boolean getFueSpam() {
        return aceptada;
    }

    public void setFueSpam(Boolean aceptada) {
        this.aceptada = aceptada;
    }
}
