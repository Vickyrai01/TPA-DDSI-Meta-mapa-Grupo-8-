package models.entities.hecho;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEtiquetas {

    private Hecho hecho;
    private List<Etiqueta> etiquetaList;
    private EtiquetaCategoria etiquetaCategoria;
    private EtiquetaFecha etiquetaFecha;
    private EtiquetaLugar etiquetaLugar;

    @BeforeEach
    void setUp() {
        // Configuración inicial para cada test
        etiquetaList = new ArrayList<>();

        etiquetaCategoria = new EtiquetaCategoria("categoria1");
        etiquetaCategoria.setKeyword("categoria");
        etiquetaList.add(etiquetaCategoria);

        etiquetaFecha = new EtiquetaFecha(LocalDate.of(2023, 1, 1));
        etiquetaFecha.setKeyword("Fecha");
        etiquetaList.add(etiquetaFecha);

        Coordenadas coordenadas = new Coordenadas(40.7128, -74.0060);
        etiquetaLugar = new EtiquetaLugar(coordenadas);
        etiquetaLugar.setKeyword("Lugar");
        etiquetaList.add(etiquetaLugar);

        hecho = new Hecho(
                "Título del hecho",
                "Descripción del hecho",
                etiquetaList,
                null,
                LocalDate.now(),
                null,
                null,
                null,
                null,
                null
        );
    }

    @Test
    void testModificarEtiquetaCategoria() {
        // Act
        hecho.modificarEtiquetaCategoria(etiquetaCategoria, "nuevaCategoria");

        // Assert
        assertEquals("nuevaCategoria", etiquetaCategoria.getNombre());
        // Verificar que solo se modificó la etiqueta de categoría
        assertEquals("categoria", etiquetaCategoria.getKeyword());
        assertEquals(LocalDate.of(2023, 1, 1), etiquetaFecha.getFechaAcontecimiento());
    }

    @Test
    void testModificarEtiquetaFecha() {
        // Arrange
        LocalDate nuevaFecha = LocalDate.of(2024, 6, 1);

        // Act
        hecho.modificarEtiquetaFecha(etiquetaFecha, nuevaFecha);

        // Assert
        assertEquals(nuevaFecha, etiquetaFecha.getFechaAcontecimiento());
        // Verificar que solo se modificó la etiqueta de fecha
        assertEquals("categoria1", etiquetaCategoria.getNombre());
        assertEquals(40.7128, etiquetaLugar.getCoordenadasLugar().getLatitud());
    }


    @Test
    void testModificarEtiquetaLugar() {
        // Arrange
        double nuevaLatitud = 34.0522;
        double nuevaLongitud = -118.2437;

        // Act
        hecho.modificarEtiquetaLugar(etiquetaLugar, nuevaLatitud, nuevaLongitud);

        // Assert
        assertEquals(nuevaLatitud, etiquetaLugar.getCoordenadasLugar().getLatitud());
        assertEquals(nuevaLongitud, etiquetaLugar.getCoordenadasLugar().getLongitud());
        // Verificar que solo se modificó la etiqueta de lugar
        assertEquals("categoria1", etiquetaCategoria.getNombre());
        assertEquals(LocalDate.of(2023, 1, 1), etiquetaFecha.getFechaAcontecimiento());
    }

    @Test
    void testAgregarEtiqueta() {
        // Arrange
        int cantidadInicial = hecho.getEtiquetas().size();
        EtiquetaCategoria nuevaEtiqueta = new EtiquetaCategoria("nuevaCategoria");
        nuevaEtiqueta.setKeyword("categoria");

        // Act
        hecho.agregarEtiqueta(nuevaEtiqueta);

        // Assert
        assertEquals(cantidadInicial + 1, hecho.getEtiquetas().size());
        assertTrue(hecho.getEtiquetas().contains(nuevaEtiqueta));
    }
    
}
