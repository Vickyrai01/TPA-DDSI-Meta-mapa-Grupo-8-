package core.api.handlers.hechos;

import core.api.DTO.HechoResumenDTO;
import core.models.entities.hecho.Estado;
import core.models.entities.hecho.Hecho;
import core.models.repository.HechosRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetHechoHandler implements Handler {

    // Instancia del Singleton Repository
    private final HechosRepository repoHechos = HechosRepository.getInstance();

    @Override
    public void handle(@NotNull Context context) throws Exception {
        /* ... (Tu código comentado de filtros se mantiene igual si quieres guardarlo) ...
         */

        // 1. CAMBIO CLAVE: Usamos el método optimizado con JOIN FETCH
        // En lugar de repoHechos.obtenerTodas(), usamos findAllConMultimedia()
        List<Hecho> hechosTotales = repoHechos.findAllConMultimedia();

        // 2. Filtrado en memoria (Java)
        // Mantenemos tu lógica actual: filtrar solo los ACEPTADOS
        List<Hecho> hechosAprobados = hechosTotales.stream()
                .filter(hecho -> Estado.ACEPTADO.equals(hecho.getEstado()))
                .toList();

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

        // Filtrar por contribuyente si se pasa el parámetro
        String contribuyenteParam = context.queryParam("contribuyente");
        List<Hecho> hechosFiltrados;
        if (contribuyenteParam != null && !contribuyenteParam.isBlank()) {
            hechosFiltrados = hechosAprobados.stream()
                    .filter(h -> h.getContribuyente() != null && contribuyenteParam.equalsIgnoreCase(h.getContribuyente().getNombreCompleto()))
                    .toList();
        } else {
            hechosFiltrados = hechosAprobados;
        }

        List<HechoResumenDTO> hechosDevolver = pasarDTO(hechosFiltrados);
        context.json(hechosDevolver);
    }

    public List<HechoResumenDTO> pasarDTO(List<Hecho> hechos){
        // Mapeo simple usando el método estático 'from' de tu DTO
        return hechos.stream().map(HechoResumenDTO::from).toList();
    }
}