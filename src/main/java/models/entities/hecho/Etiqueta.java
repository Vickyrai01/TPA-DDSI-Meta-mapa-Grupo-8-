package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;


public abstract class Etiqueta {
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Setter
    @Getter
    private String keyword;

    public boolean esEtiqueta(String tipoEtiqueta){
        return Objects.equals(tipoEtiqueta, keyword);
    }

    public void cambiarCategoria(String categoriaNueva){};

    public void cambiarUbicacion(Double latitud, Double longitud){};

    public void cambiarFecha(LocalDate fechaNueva){};
}
