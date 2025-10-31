package core.api.handlers.hechos;

import core.api.DTO.HechoResumenDTO;
import core.api.handlers.colecciones.UtilsFormatos;
import core.models.entities.hecho.Estado;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.CriterioFechaCarga;
import core.models.entities.colecciones.criterios.CriterioFechaSuceso;
import core.models.entities.colecciones.criterios.FiltradorColecciones;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GetHechoHandler implements Handler {
    private final HechosRepository repoHechos = HechosRepository.getInstance();


    @Override
    public void handle(@NotNull Context context) throws Exception {
        /*
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

        List<Hecho> hechosTotales = repoHechos.obtenerTodas();
        List<Hecho> hechosFiltrados = FiltradorColecciones.getInstance().filtrarHechos(hechosTotales, criterios);

        context.json(hechosFiltrados);
        */
        List<Hecho> hechosTotales = repoHechos.obtenerTodas();
        List<Hecho> hechosAprobados = hechosTotales.stream().filter(hecho -> hecho.getEstado().equals(Estado.ACEPTADO)).toList();
        List<HechoResumenDTO> hechosDevolver = pasarDTO(hechosAprobados);
        context.json(hechosDevolver);
    }

    public List<HechoResumenDTO> pasarDTO(List<Hecho> hechos){
        return hechos.stream().map(HechoResumenDTO::from).toList();
    }

}
