package models.entities.fuentes;

import java.util.concurrent.atomic.AtomicInteger;

public class FuenteFactory {
    private static AtomicInteger contadorCSV= new AtomicInteger(0);
    private static AtomicInteger contadorAPI= new AtomicInteger(0);


    public static Fuente crearFuente(String nombre, String link, TipoFuente tipo, TipoConexion strategy){
        StrategyTipoConexion strategyTipoConexion = crearStrategy(strategy);
        String codigo = generarCodigo(strategy);
        Fuente fuente = new Fuente();
        fuente.setNombre(nombre);
        fuente.setLink(link);
        fuente.setTipoFuente(tipo);
        fuente.setStrategyTipoConexion(strategyTipoConexion);
        fuente.setCodigoDeFuente(codigo);
        return fuente;
    }

    private static StrategyTipoConexion crearStrategy(TipoConexion tipoConexion) {
        switch (tipoConexion) {
            case CSV:
                return new StrategyCSV();
            case APIREST:
                return new StrategyAPIREST();
            default:
                throw new IllegalArgumentException("Tipo de conexión no soportada: " + tipoConexion);
        }
    }

    private static String generarCodigo(TipoConexion tipoConexion) {
        switch (tipoConexion) {
            case CSV: return "C" + contadorCSV.incrementAndGet();
            case APIREST: return "A" + contadorAPI.incrementAndGet();
            default:
                throw new IllegalArgumentException("Tipo de conexión no soportada: " + tipoConexion);
    }
}}

