package models.entities.normalizador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.*;
import java.time.temporal.ChronoField;

public class NormalizadorFecha {

    private static volatile NormalizadorFecha instance;

    private NormalizadorFecha() {}

    public static NormalizadorFecha getInstance() {
        if (instance == null) {
            synchronized (NormalizadorFecha.class) {
                if (instance == null) {
                    instance = new NormalizadorFecha();
                }
            }
        }
        return instance;
    }

    //Despues del 30, en caso de que sea AA/MM/DD, se considera que era 1900.
    //Si es del 00-29 es del 2000.
    private static final int ANIO_BASE_DOS_DIGITOS = 1930;

    private static final DateTimeFormatter FORMATO_ISO_SALIDA = DateTimeFormatter.ISO_LOCAL_DATE;

    // yyyy/M/d
    private static final DateTimeFormatter ANIO_MES_DIA_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // d/M/yyyy
    private static final DateTimeFormatter DIA_MES_ANIO_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // M/d/yyyy
    private static final DateTimeFormatter MES_DIA_ANIO_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // d/M/yy
    private static final DateTimeFormatter DIA_MES_ANIO_2 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValueReduced(ChronoField.YEAR, 2, 2, ANIO_BASE_DOS_DIGITOS)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // M/d/yy
    private static final DateTimeFormatter MES_DIA_ANIO_2 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValueReduced(ChronoField.YEAR, 2, 2, ANIO_BASE_DOS_DIGITOS)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    public static String normalizarAIso(String raw) {
        LocalDate fechaActual = LocalDate.now(ZoneId.systemDefault());
        if (raw == null) throw new IllegalArgumentException("Fecha nula");
        String fechaNormalizada = raw.trim().replace('-', '/');
        String[] partesFecha = fechaNormalizada.split("/");
        if (partesFecha.length != 3) throw new IllegalArgumentException("Formato no reconocido: " + raw);

        boolean anioPrimero = partesFecha[0].length() == 4;
        boolean anio2digitos = partesFecha[2].length() <= 2;

        //YYYY/MM/DD (no ambiguo)
        if (anioPrimero) {
            return FORMATO_ISO_SALIDA.format(LocalDate.parse(fechaNormalizada, ANIO_MES_DIA_4));
        }

        int a, b;
        try { a = Integer.parseInt(partesFecha[0]);
              b = Integer.parseInt(partesFecha[1]); }
        catch (NumberFormatException e)
        { throw new IllegalArgumentException("Componentes de fecha no numéricos: " + raw);}

        // CASOS NO AMBIGUOS. DESCARTE POR RANGO DE VALORES:
        if (a > 12 && b <= 12) { // DMY
            return FORMATO_ISO_SALIDA.format(LocalDate.parse(fechaNormalizada, anio2digitos ? DIA_MES_ANIO_2 : DIA_MES_ANIO_4));
        }

        if (b > 12 && a <= 12) { // MDY
            return FORMATO_ISO_SALIDA.format(LocalDate.parse(fechaNormalizada, anio2digitos ? MES_DIA_ANIO_2 : MES_DIA_ANIO_4));
        }

        // Caso ultra ambiguo (a <= 12 && b <= 12)
        return resolverFechaAmbigua(fechaNormalizada, anio2digitos, fechaActual, raw);
    }

    private static String resolverFechaAmbigua(String fecha, boolean anio2digitos,
                                               LocalDate fechaActual, String raw) {
        LocalDate fechaDiaMesAnio = null;
        LocalDate fechaMesDiaAnio = null;

        try {
            fechaDiaMesAnio = LocalDate.parse(fecha,
                    anio2digitos ? DIA_MES_ANIO_2 : DIA_MES_ANIO_4);
        } catch (DateTimeParseException e) {
            // No se puede parsear como DD/MM/YYYY
        }

        try {
            fechaMesDiaAnio = LocalDate.parse(fecha,
                    anio2digitos ? MES_DIA_ANIO_2 : MES_DIA_ANIO_4);
        } catch (DateTimeParseException e) {
            // No se puede parsear como MM/DD/YYYY
        }

        // Si solo una interpretación es válida
        if (fechaDiaMesAnio != null && fechaMesDiaAnio == null) {
            return FORMATO_ISO_SALIDA.format(fechaDiaMesAnio);
        }
        if (fechaMesDiaAnio != null && fechaDiaMesAnio == null) {
            return FORMATO_ISO_SALIDA.format(fechaMesDiaAnio);
        }
        if (fechaDiaMesAnio == null && fechaMesDiaAnio == null) {
            throw new IllegalArgumentException("Fecha inválida: " + raw);
        }

        // Ambas interpretaciones son válidas, usar lógica de fecha futura/pasada
        boolean esFechaDiaMesFutura = fechaDiaMesAnio.isAfter(fechaActual);
        boolean esFechaMesDiaFutura = fechaMesDiaAnio.isAfter(fechaActual);

        if (esFechaDiaMesFutura && !esFechaMesDiaFutura) {
            return FORMATO_ISO_SALIDA.format(fechaMesDiaAnio); // DMY es futura → probablemente es MDY
        }
        if (esFechaMesDiaFutura && !esFechaDiaMesFutura) {
            return FORMATO_ISO_SALIDA.format(fechaDiaMesAnio); // MDY es futura → probablemente es DMY
        }

        //Ambas son futuras o ambas son pasadas → no se puede determinar automáticamente
        throw new ExcepcionFechaAmbigua(
                "Fecha ambigua: \"" + raw + "\" puede ser " +
                        fechaDiaMesAnio + " (DD/MM/YYYY) o " + fechaMesDiaAnio + " (MM/DD/YYYY). " +
                        "Se requiere revisión manual."
        );
    }

    public LocalDate normalizarFecha(String fecha) throws ExcepcionRevisionManualFecha {
        try {
            String fechaIso = normalizarAIso(fecha);
            return LocalDate.parse(fechaIso, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch(ExcepcionFechaAmbigua e) {
            throw new ExcepcionRevisionManualFecha(e.getMessage());
        }
    }

    public static class ExcepcionFechaAmbigua extends RuntimeException {
        public ExcepcionFechaAmbigua(String mensaje) {
            super(mensaje);
        }
    }

    public static class ExcepcionRevisionManualFecha extends Exception {
        public ExcepcionRevisionManualFecha(String mensaje) {
            super(mensaje);
        }
    }
}
