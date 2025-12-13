package core.models.agregador.normalizador;

import java.time.LocalTime;

public class NormalizadorHora {

    private static NormalizadorHora instance;

    public static NormalizadorHora getInstance() {
        if (instance == null) instance = new NormalizadorHora();

        return instance;
    }

    public LocalTime normalizarHora(String horaString){
        return (horaString != null || horaString.equals(""))
                ? LocalTime.parse(horaString)
                : null;
    }
}
