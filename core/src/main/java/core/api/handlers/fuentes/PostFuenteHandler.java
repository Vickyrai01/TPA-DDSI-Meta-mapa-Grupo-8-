package core.api.handlers.fuentes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.DTO.FuenteDTO;
import core.api.DTO.HechoAIntegrarDINAMICO;
import core.api.handlers.colecciones.PatchAgregarFuentesColeccionHandler;
import core.models.entities.fuentes.*;
import core.models.repository.FuentesRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PostFuenteHandler implements Handler {

FuentesRepository fuentesRepository = FuentesRepository.getInstance();

@Override
    public void handle(@NotNull Context context) throws Exception {
        FuenteDTO dto = context.bodyAsClass(FuenteDTO.class);
        System.out.println("Creando fuente: " + context.body());

    StrategyTipoConexion strategyTipoConexion = strategyStringToStrategy(dto.getStrategyTipoConexion());
    TipoFuente tipoFuente = fromString(dto.getTipoFuente());

        Fuente fuenteDTO = new Fuente(
                dto.getNombre(),
                dto.getLink(),
                tipoFuente,
                strategyTipoConexion
        );

        fuentesRepository.add(fuenteDTO);
        context.status(201);
    }

    public static TipoFuente fromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return TipoFuente.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Podrías registrar un warning aquí si querés
            return null;
        }
    }

    public StrategyTipoConexion strategyStringToStrategy(String strategy) {
        if (strategy == "API REST") {
            StrategyTipoConexion strategyTipoConexion = new StrategyAPIREST();
        }
    if (strategy == "CSV"){
        StrategyTipoConexion strategyTipoConexion = new StrategyCSV();
    }
    return null;
    }
}
