package servicioEstadisticas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seeders.RepositoryServicioEstadisticasSeeder;
import servicioEstadisticas.model.ServicioEstadisticas;

import static org.junit.jupiter.api.Assertions.*;

public class ServicioEstadisticasTest {

    private ServicioEstadisticas servicioEstadisticas;
    private RepositoryServicioEstadisticasSeeder seeder;

    @BeforeEach
    void setUp() {

        servicioEstadisticas = ServicioEstadisticas.getInstance();
        //Si usas el update en el persistence.xml entonces tenes que dejar comentado el seeder, pero si esta en create entonces hay que descomentarlo
        //seeder = RepositoryServicioEstadisticasSeeder.getInstance();
        //seeder.cargarHechos();
        servicioEstadisticas.actualizarEstadisticas();
    }

    @Test
    void testCategoriaMasReportada() {
        String categoria = servicioEstadisticas.getCategoriaMasReportada();
        assertNotNull(categoria);
        assertEquals("Accidente vial", categoria);
    }

    @Test
    void testProvinciaConMasHechos() {
        String provincia = servicioEstadisticas.getProvinciaConMasHechos();
        assertNotNull(provincia);
        assertEquals("Buenos Aires", provincia);
    }

    @Test
    void testHorarioMasFrencuentePorCategoria() {
        String resultado = servicioEstadisticas.horarioxCategoria("Accidente vial");
        assertNotNull(resultado);
        assertTrue(resultado.contains("8:00"));
    }

    @Test
    void testProvinciaMasHechosPorCategoria() {
        String resultado = servicioEstadisticas.provicniaConMasHechosEnCategoria("Incendio forestal");
        assertNotNull(resultado);
        // Según tus datos, deberías saber qué provincia tiene más incendios forestales
        assertEquals("Misiones", resultado);
    }

    @Test
    void testCantidadSolicitudesEliminacion() {
        Integer cantidad = servicioEstadisticas.getCantSolicitudesEliminacion();
        assertNotNull(cantidad);
        assertTrue(cantidad >= 0);
    }

}
