package models.entities.fuentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@AllArgsConstructor
public class Fuente {
    @Setter
    @Getter
    private String nombre;
    @Setter
    @Getter
    private URL link;
    @Setter
    @Getter
    private TipoFuente tipoFuente;
    @Setter
    @Getter
    private StrategyTipoConexion strategyTipoConexion;
}

