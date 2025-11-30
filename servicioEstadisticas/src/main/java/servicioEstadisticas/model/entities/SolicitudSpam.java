package servicioEstadisticas.model.entities;

import javax.persistence.*;


@Entity
@Table(name = "solicitud_spam")
public class SolicitudSpam {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_spam;

    @Column(name = "fue_spam")
    private Boolean fueSpam;


    public SolicitudSpam(Boolean fueSpam) {
        this.fueSpam = fueSpam;
    }


    public Integer getId_spam() {
        return id_spam;
    }

    public void setId_spam(Integer id_spam) {
        this.id_spam = id_spam;
    }

    public Boolean getFueSpam() {
        return fueSpam;
    }

    public void setFueSpam(Boolean fueSpam) {
        this.fueSpam = fueSpam;
    }
}
