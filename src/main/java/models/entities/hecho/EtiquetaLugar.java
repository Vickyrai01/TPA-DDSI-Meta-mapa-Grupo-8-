package models.entities.hecho;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



public class EtiquetaLugar extends Etiqueta {
    public Coordenadas getCoordenadasLugar() {
        return coordenadasLugar;
    }

    @Setter
    private Coordenadas coordenadasLugar;

    public EtiquetaLugar(Coordenadas coordenadasLugar) {
        this.coordenadasLugar = coordenadasLugar;
    }

    public void cambiarUbicacion(Double latitud, Double longitud){
        coordenadasLugar.cambiarUbicacion(latitud, longitud);
    }

}
