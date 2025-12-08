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

        // 3. Conversión a DTO y Respuesta
        List<HechoResumenDTO> hechosDevolver = pasarDTO(hechosAprobados);
        context.json(hechosDevolver);
    }

    public List<HechoResumenDTO> pasarDTO(List<Hecho> hechos){
        // Mapeo simple usando el método estático 'from' de tu DTO
        return hechos.stream().map(HechoResumenDTO::from).toList();
    }
}