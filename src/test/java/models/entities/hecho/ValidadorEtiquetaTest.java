package models.entities.hecho;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorEtiquetasTest {

    private ValidadorDeEtiqueta validador = new ValidadorDeEtiqueta();

    @Test
    void testNoMasDeUnLugarConCeroEtiquetas() {
        List<Etiqueta> etiquetas = new ArrayList<>();
        assertTrue(validador.noMasDeUnLugar(etiquetas));
    }

    @Test
    void testNoMasDeUnLugarConUnaEtiqueta() {
        List<Etiqueta> etiquetas = new ArrayList<>();
        EtiquetaLugar lugar = new EtiquetaLugar(new Coordenadas(10.0, 10.0));
        lugar.setKeyword("Lugar");
        etiquetas.add(lugar);

        assertTrue(validador.noMasDeUnLugar(etiquetas));
    }

    @Test
    void testNoMasDeUnLugarConDosEtiquetas() {
        List<Etiqueta> etiquetas = new ArrayList<>();
        EtiquetaLugar lugar1 = new EtiquetaLugar(new Coordenadas(10.0, 10.0));
        lugar1.setKeyword("Lugar");
        EtiquetaLugar lugar2 = new EtiquetaLugar(new Coordenadas(20.0, 20.0));
        lugar2.setKeyword("Lugar");
        etiquetas.add(lugar1);
        etiquetas.add(lugar2);

        assertFalse(validador.noMasDeUnLugar(etiquetas));
    }

    @Test
    void testNoMasDeUnHorarioConUnaEtiqueta() {
        List<Etiqueta> etiquetas = new ArrayList<>();
        Etiqueta horario = new Etiqueta() {};
        horario.setKeyword("Horario");
        etiquetas.add(horario);

        assertTrue(validador.noMasDeUnHorario(etiquetas));
    }

    @Test
    void testTieneUnoDeCadaTipo() {
        List<Etiqueta> etiquetas = new ArrayList<>();

        // Crear etiquetas con los keywords esperados
        Etiqueta horario = new Etiqueta() {};
        horario.setKeyword("Horario");

        EtiquetaLugar lugar = new EtiquetaLugar(new Coordenadas(10.0, 10.0));
        lugar.setKeyword("Lugar");

        EtiquetaFecha fecha = new EtiquetaFecha(LocalDate.now());
        fecha.setKeyword("Fecha");

        etiquetas.add(horario);
        etiquetas.add(lugar);
        etiquetas.add(fecha);

        assertTrue(validador.tieneUnoDeCadaTipo(etiquetas));
    }

    @Test
    void testTieneUnoDeCadaTipoFaltaUna() {
        List<Etiqueta> etiquetas = new ArrayList<>();

        Etiqueta horario = new Etiqueta() {};
        horario.setKeyword("Horario");

        EtiquetaLugar lugar = new EtiquetaLugar(new Coordenadas(10.0, 10.0));
        lugar.setKeyword("Lugar");

        etiquetas.add(horario);
        etiquetas.add(lugar);

        assertFalse(validador.tieneUnoDeCadaTipo(etiquetas));
    }

    @Test
    void testContarEtiquetasPorTipo() {
        List<Etiqueta> etiquetas = new ArrayList<>();

        Etiqueta etiqueta1 = new Etiqueta() {};
        etiqueta1.setKeyword("Tipo1");

        Etiqueta etiqueta2 = new Etiqueta() {};
        etiqueta2.setKeyword("Tipo1");

        Etiqueta etiqueta3 = new Etiqueta() {};
        etiqueta3.setKeyword("Tipo2");

        etiquetas.add(etiqueta1);
        etiquetas.add(etiqueta2);
        etiquetas.add(etiqueta3);

        assertEquals(2, validador.contarEtiquetasPorTipo(etiquetas, "Tipo1"));
        assertEquals(1, validador.contarEtiquetasPorTipo(etiquetas, "Tipo2"));
        assertEquals(0, validador.contarEtiquetasPorTipo(etiquetas, "TipoInexistente"));
    }
}