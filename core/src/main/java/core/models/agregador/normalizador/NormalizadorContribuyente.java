package core.models.agregador.normalizador;

import core.models.entities.hecho.Contribuyente;

public class NormalizadorContribuyente {

    private static NormalizadorContribuyente instance;

    public static NormalizadorContribuyente getInstance() {
        if (instance == null) instance = new NormalizadorContribuyente();

        return instance;
    }

    public Contribuyente obtenerContribuyente(String raw){
        if (raw == null || raw.isBlank()) return null;

        Contribuyente c = new Contribuyente();

        String trimmed = raw.trim();

        if (trimmed.contains("@")) {
            // Es un mail
            c.setMail(trimmed);
        } else {
            // Es un apellido
            c.setApellido(trimmed);
        }

        return c;
    }
}
