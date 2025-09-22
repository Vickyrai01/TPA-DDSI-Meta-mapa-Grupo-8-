package core.api.handlers.colecciones;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioFechaCarga;
import core.models.entities.colecciones.criterios.CriterioFechaSuceso;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetHechosDeColeccionesHandler implements Handler {

    private final ColeccionesRepository repoColecciones = ColeccionesRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {

        UtilsFormatos utilsFormatos = new UtilsFormatos();

        String categoria = context.queryParam("categoria");
        String fechaReporteDesde = context.queryParam("fecha_reporte_desde");
        String fechaReporteHasta = context.queryParam("fecha_reporte_hasta");
        String fechaAcontecimientoDesde = context.queryParam("fecha_acontecimiento_desde");
        String fechaAcontecimientoHasta = context.queryParam("fecha_acontecimiento_hasta");
        String latitud = context.queryParam("latitud");
        String longitud = context.queryParam("longitud");

        List<Criterio> criterios = new ArrayList<>();

        if (categoria != null && !categoria.isBlank()) {
            criterios.add(utilsFormatos.transformarCategoriaEnCriterio(categoria));
        }

        if ((fechaReporteDesde != null && !fechaReporteDesde.isBlank()) || (fechaReporteHasta != null && fechaReporteHasta.isBlank())) {
            criterios.add(new CriterioFechaCarga(utilsFormatos.stringALocalDate(fechaReporteDesde), utilsFormatos.stringALocalDate(fechaReporteHasta)));
        }
        else {System.out.println("No se han pasado las fechas de reporte");}

        if (fechaAcontecimientoDesde != null || fechaAcontecimientoHasta != null) {
            criterios.add(new CriterioFechaSuceso(utilsFormatos.stringALocalDate(fechaAcontecimientoDesde), utilsFormatos.stringALocalDate(fechaAcontecimientoHasta)));
        }

        if (latitud != null && longitud != null) {criterios.add(utilsFormatos.transformarUbicacionEnCriterio(latitud, longitud));}

        Integer idBuscado = context.pathParamAsClass("id", Integer.class).get();
        Optional<Coleccion> resultadoBusqueda = repoColecciones.obtenerTodas().stream()
                .filter(c -> c.getId() == idBuscado)
                .findFirst();

        if (resultadoBusqueda.isPresent()) {
            List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarColeccion(resultadoBusqueda.get(), criterios);
            context.status(200).json(hechosFiltrados);  //solo hechos :)
        } else {
            context.status(404).result("Colección no encontrada con ID: " + idBuscado);
        }


    }
    }
