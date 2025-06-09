package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


public abstract class Etiqueta {
    @Setter
    @Getter
    private String keyword;

    public boolean esEtiqueta(String tipoEtiqueta){
        return Objects.equals(tipoEtiqueta, keyword);
    }
}
