package models.entities.coleccion;

import java.net.URL;
import java.net.MalformedURLException;
import models.entities.colecciones.Coleccion;
import models.entities.colecciones.ModoDeNavegacion;
import models.entities.colecciones.StrategyAbsoluta;

import models.entities.colecciones.criterios.*;
import models.entities.fuentes.*;
import models.entities.hecho.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import models.entities.colecciones.SchedulerActualizarVisibles;

public class TestColeccionConsenso {
    private Coleccion coleccion;
    private List<Fuente> fuentes;
    private List<Hecho> hechos;
    private List<Hecho> hechosVisibles;
    private List<Criterio> criterios;

    // Coordenadas para pruebas
    private Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    private Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    private Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);

    // Categorías para pruebas
    private Categoria categoriaIncendio = new Categoria("Incendio");
    private Categoria categoriaChoque = new Categoria("Choque");

    @BeforeEach
    void setUp() throws MalformedURLException {
        // Inicializar listas
        fuentes = new ArrayList<>();
        hechos = new ArrayList<>();
        hechosVisibles = new ArrayList<>();
        criterios = new ArrayList<>();

        // Crear fuentes con diferentes tipos de conexión
        Fuente fuente1 = new Fuente(1, "Fuente Estática 1", new URL("http://fuente1.com"), TipoFuente.ESTATICA, new StrategyCSV());
        Fuente fuente2 = new Fuente(2, "Fuente Automática 1", new URL("http://fuente2.com"), TipoFuente.DINAMICA, new StrategyAPIREST());
        Fuente fuente3 = new Fuente(3, "Fuente Estática 2", new URL("http://fuente3.com"), TipoFuente.PROXY, new StrategyBibliotecaCliente());
        fuentes.addAll(Arrays.asList(fuente1, fuente2, fuente3));

        // Crear hechos para cada fuente
        // Hechos similares reportados por diferentes fuentes
        Hecho hecho1Fuente1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Incendio grave en zona residencial", "Incendio en Barrio Norte");

        Hecho hecho1Fuente2 = new Hecho(2, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.DINAMICA, null,
                "Fuego detectado en área residencial", "Incendio en Barrio Norte");

        Hecho hecho1Fuente3 = new Hecho(3, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(1),
                TipoFuente.ESTATICA, null,
                "Se reporta incendio en vivienda", "Incendio en Barrio Norte");

        // Hechos diferentes
        Hecho hecho2 = new Hecho(4, coordenadas2, categoriaChoque, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null,
                "Colisión entre dos vehículos", "Accidente en Avenida Principal");

        Hecho hecho3 = new Hecho(5, coordenadas3, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now().minusDays(3),
                TipoFuente.PROXY, null,
                "Incendio forestal detectado", "Incendio en Reserva Natural");

        hechos.addAll(Arrays.asList(hecho1Fuente1, hecho1Fuente2, hecho1Fuente3, hecho2, hecho3));

        // Crear colección
        coleccion = new Coleccion(1, "Colección de Prueba",
                "Colección para pruebas de consenso",
                fuentes, criterios, hechos, "test-handle");

        // Configurar modo de navegación y algoritmo de consenso
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;
        coleccion.algoritmoConsenso = new StrategyAbsoluta(); // Asumiendo que existe esta clase
    }

    /*

    hechos iguales
    hechos similares
    que si el algortimo es null te devuelven todos los hechos
    si no es curada te devuelve todos los hechos
    Se ejecute bien el algortimo correspondiente
    Que no se ejecute si es una hora fuera de las de baja carga

    */

    @Test
    void testHechosIguales() {
        // Crear dos hechos idénticos
        Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now(),
                TipoFuente.ESTATICA, null,
                "Incendio grave", "Incendio en Barrio Norte");

        Hecho hecho2 = new Hecho(2, coordenadas1, categoriaIncendio, null,
                LocalDate.now(), null, Estado.ACEPTADO, null,
                LocalDate.now(), LocalDate.now(),
                TipoFuente.ESTATICA, null,
                "Incendio grave", "Incendio en Barrio Norte");

        assertTrue(coleccion.algoritmoConsenso.sonHechosIguales(hecho1, hecho2));
    }

    @Test
    void testHechosSimilares() {
        // Crear dos hechos similares pero no idénticos
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAyer = fechaHoy.minusDays(1);

        Hecho hecho1 = new Hecho(1, coordenadas1, categoriaIncendio, null,
                fechaHoy, null, Estado.ACEPTADO, null,
                fechaHoy, fechaHoy,
                TipoFuente.ESTATICA, null,
                "Incendio grave", "Incendio en Barrio Norte");

        Hecho hecho2 = new Hecho(2, coordenadas2, categoriaIncendio, null,
                fechaAyer, null, Estado.ACEPTADO, null,
                fechaAyer, fechaAyer,
                TipoFuente.ESTATICA, null,
                "Fuego en edificio", "Incendio en zona norte");

        assertTrue(coleccion.algoritmoConsenso.sonHechosSimilares(hecho1, hecho2));
    }

    @Test
    void testAlgoritmoNull() {
        // Configurar algoritmo como null
        coleccion.algoritmoConsenso = null;
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;

        // Actualizar colección
        coleccion.actualizarColeccionVisible(fuentes, hechos);

        // Verificar que devuelve todos los hechos
        assertEquals(hechos.size(), coleccion.hechosVisibles.size());
        assertTrue(coleccion.hechosVisibles.containsAll(hechos));
    }

    @Test
    void testModoNoCurado() {
        // Configurar modo no curado
        coleccion.modoDeNavegacion = ModoDeNavegacion.IRRESTRICTO;

        // Actualizar colección
        coleccion.actualizarColeccionVisible(fuentes, hechos);

        // Verificar que devuelve todos los hechos
        assertEquals(hechos.size(), coleccion.hechosVisibles.size());
        assertTrue(coleccion.hechosVisibles.containsAll(hechos));
    }

    @Test
    void testEjecucionAlgoritmo() {
        // Configurar algoritmo y modo
        coleccion.algoritmoConsenso = new StrategyAbsoluta();
        coleccion.modoDeNavegacion = ModoDeNavegacion.CURADA;

        // Actualizar colección
        coleccion.actualizarColeccionVisible(fuentes, hechos);

        // Verificar que el algoritmo filtró los hechos
        assertTrue(coleccion.hechosVisibles.size() < hechos.size());
    }
}