package core.models.agregador.normalizador;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;

import java.time.LocalDate;

//Esto va a pasar un hecho DTO a un hecho
public class FactoryHecho {
     private static volatile FactoryHecho instance;

        public FactoryHecho() {
            if (instance != null) {
                throw new RuntimeException("Usa getInstance() para obtener el Singleton");
            }
        }

        public static FactoryHecho getInstance() {
            if (instance == null) {
                synchronized (FactoryHecho.class) {
                    if (instance == null) {
                        instance = new FactoryHecho();
                    }
                }
            }
            return instance;
        }

    public Hecho convertirHecho(HechoAIntegrarDTO hecho, LocalDate fecha, Categoria categoria, Coordenadas ubicacion){
        TipoFuente tipoFuente = TipoFuente.valueOf(hecho.getTipoFuente());

        return new Hecho(
                null, //Analizar como asignar el ID
                ubicacion,
                categoria,
                null,
                LocalDate.now(),
                null,
                Estado.ACEPTADO,
                null,
                LocalDate.now(),
                fecha,
                tipoFuente,
                null,
                hecho.getDescripcion(),
                hecho.getTitulo(),
                null,
                hecho.getHash(),
                hecho.getIdFuente()
        );
    }

}
