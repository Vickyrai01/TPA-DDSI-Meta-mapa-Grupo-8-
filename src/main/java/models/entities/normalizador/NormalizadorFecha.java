package models.entities.normalizador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

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

    /** Excepción para forzar revisión manual cuando la fecha ambigua no se puede desambiguar. */
    public static class NeedsManualReviewException extends RuntimeException {
        public NeedsManualReviewException(String msg) { super(msg); }
    }

    // Ventana para años de 2 dígitos: 00–49 => 2000–2049; 50–99 => 1950–1999
    private static final int TWO_DIGIT_YEAR_BASE = 1930;

    private static final DateTimeFormatter ISO_OUT = DateTimeFormatter.ISO_LOCAL_DATE;

    // yyyy/M/d
    private static final DateTimeFormatter YMD_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // d/M/yyyy
    private static final DateTimeFormatter DMY_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // M/d/yyyy
    private static final DateTimeFormatter MDY_4 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.YEAR, 4)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // d/M/yy
    private static final DateTimeFormatter DMY_2 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValueReduced(ChronoField.YEAR, 2, 2, TWO_DIGIT_YEAR_BASE)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    // M/d/yy
    private static final DateTimeFormatter MDY_2 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral('/')
            .appendValueReduced(ChronoField.YEAR, 2, 2, TWO_DIGIT_YEAR_BASE)
            .toFormatter().withResolverStyle(ResolverStyle.STRICT);

    public static String toIsoOrReview(String raw) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        if (raw == null) throw new IllegalArgumentException("Fecha nula");
        String s = raw.trim().replace('-', '/');
        String[] parts = s.split("/");
        if (parts.length != 3) throw new IllegalArgumentException("Formato no reconocido: " + raw);

        boolean yearFirst = parts[0].length() == 4;
        boolean year2 = parts[2].length() <= 2;

        if (yearFirst) {
            // yyyy/M/d (no ambiguo)
            return ISO_OUT.format(LocalDate.parse(s, YMD_4));
        }

        int a, b;
        try { a = Integer.parseInt(parts[0]); b = Integer.parseInt(parts[1]); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Partes no numéricas: " + raw); }

        // No ambiguo por rango:
        if (a > 12 && b <= 12) { // DMY
            return ISO_OUT.format(LocalDate.parse(s, year2 ? DMY_2 : DMY_4));
        }
        if (b > 12 && a <= 12) { // MDY
            return ISO_OUT.format(LocalDate.parse(s, year2 ? MDY_2 : MDY_4));
        }

        // Ambiguo (a <= 12 && b <= 12): probamos ambas interpretaciones
        LocalDate dmy, mdy;
        try { dmy = LocalDate.parse(s, year2 ? DMY_2 : DMY_4); } catch (DateTimeParseException e) { dmy = null; }
        try { mdy = LocalDate.parse(s, year2 ? MDY_2 : MDY_4); } catch (DateTimeParseException e) { mdy = null; }

        // Si solo una parsea, usamos esa
        if (dmy != null && mdy == null) return ISO_OUT.format(dmy);
        if (mdy != null && dmy == null) return ISO_OUT.format(mdy);
        if (dmy == null && mdy == null) throw new IllegalArgumentException("Fecha inválida: " + raw);

        // — Regla que pediste —
        boolean dmyFuture = dmy.isAfter(today);
        boolean mdyFuture = mdy.isAfter(today);

        if (dmyFuture && !mdyFuture) return ISO_OUT.format(mdy); // DMY se pasa → es MDY
        if (mdyFuture && !dmyFuture) return ISO_OUT.format(dmy); // MDY se pasa → es DMY

        // Ambas pasadas o ambas futuras ⇒ no se puede decidir
        throw new NeedsManualReviewException(
                "Ambiguo: \"" + raw + "\" puede ser " + dmy + " (DMY) o " + mdy + " (MDY). Revisión manual requerida.");
    }

    public LocalDate normalizarFecha(String fecha) throws RevisionManualDeFechas {
        try {
            String iso = toIsoOrReview(fecha);
            return LocalDate.parse(iso, DateTimeFormatter.ISO_LOCAL_DATE);
        }catch(NeedsManualReviewException e){
            throw new RevisionManualDeFechas(e.getMessage());
        }
    }

    public class RevisionManualDeFechas extends Exception{
        public RevisionManualDeFechas(String msg) { super(msg); }
    }
}
