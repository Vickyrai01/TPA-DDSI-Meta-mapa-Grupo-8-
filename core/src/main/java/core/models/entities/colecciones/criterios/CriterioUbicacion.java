package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Hecho;
import javax.persistence.*;

@Entity
@DiscriminatorValue("UBICACION")
public class CriterioUbicacion extends Criterio {
    private String provincia;

    public CriterioUbicacion(String provincia){
        this.provincia = provincia;
    }
    public CriterioUbicacion(){}

    @Override
    public boolean cumpleCriterio(Hecho hecho) {
        return hecho.getProvincia() != null
                && hecho.getProvincia() != null
                && hecho.getProvincia().equalsIgnoreCase(this.provincia);
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia){
        this.provincia = provincia;
    }
}
