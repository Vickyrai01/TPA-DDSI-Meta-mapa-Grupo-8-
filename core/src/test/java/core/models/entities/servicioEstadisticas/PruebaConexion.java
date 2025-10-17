package core.models.entities.servicioEstadisticas;

import core.models.servicioEstadistica.DTOHechoAgregado;
import core.models.servicioEstadistica.DTOSolicitudEliminacion;
import core.models.servicioEstadistica.HandlerServicioEstadistica;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class PruebaConexion {

    DTOHechoAgregado dtoPrueba = new DTOHechoAgregado();

    HandlerServicioEstadistica handlerServicioEstadistica = new HandlerServicioEstadistica();

    public void pruebaAgregarHecho() throws IOException, InterruptedException {
        dtoPrueba.setDia(LocalDateTime.now().toString());
        dtoPrueba.setProvincia("Buenos Aires");
        dtoPrueba.setCategoria("incendio");
        dtoPrueba.setId_hecho("1234579");
        System.out.println("Enviando.... " + dtoPrueba.toString());
        handlerServicioEstadistica.enviarHechoAgregado(dtoPrueba);
    }

    DTOSolicitudEliminacion dtoSolicitudEliminacion = new DTOSolicitudEliminacion();

    public void pruebaSolicitudEliminacion() throws IOException, InterruptedException {
        dtoSolicitudEliminacion.setFueSpam(true);
        System.out.println("Enviando.... " + dtoSolicitudEliminacion.toString());
        handlerServicioEstadistica.enviarSolicitudEliminacion(dtoSolicitudEliminacion);
    }

    @Test
    void pruebaConexion() throws IOException, InterruptedException {
        PruebaConexion pruebaConexion = new PruebaConexion();
        pruebaConexion.pruebaAgregarHecho();
        pruebaConexion.pruebaSolicitudEliminacion();
        System.out.println("Prueba de conexion exitosa");
    }

}
