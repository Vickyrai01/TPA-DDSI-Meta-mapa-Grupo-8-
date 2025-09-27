package core.models.entities.colecciones.criterios;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.models.entities.hecho.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("DESCRIPCION")
public class CriterioDescripcion extends Criterio {
    private String palabraClave;

    @Id
    private Long id;

    public CriterioDescripcion(String palabraClave) {
        this.palabraClave = palabraClave;
    }

    public CriterioDescripcion() {

    }

    @JsonSerialize(as = String.class)
    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getDescripcion().toLowerCase().contains(palabraClave.toLowerCase());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
