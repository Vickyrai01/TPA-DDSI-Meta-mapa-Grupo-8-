package core.models.entities.colecciones.criterios;

import core.api.DTO.FiltroHechoDTO;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.hecho.Hecho;

import java.util.List;
import java.util.stream.Collectors;

public class FiltradorColecciones {

    private static volatile FiltradorColecciones instance;

    private FiltradorColecciones() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FiltradorColecciones getInstance() {
        if (instance == null) {
            synchronized (FiltradorColecciones.class) {
                if (instance == null) {
                    instance = new FiltradorColecciones();
                }
            }
        }
        return instance;
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos, List<Criterio> criterios) {
        if(criterios == null || criterios.isEmpty())
        {return hechos;}
        else {
            return hechos.stream()
                    .filter(hecho -> criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho)))
                    .collect(Collectors.toList());
        }
    }

    public List<Hecho> filtrarColeccion(Coleccion coleccion, List<Criterio> criterios)
    {
        if(criterios == null || criterios.isEmpty())
        {return coleccion.getHechos();}
        else {
        return filtrarHechos(coleccion.getHechos(), criterios);}
    }

    public List<Hecho> filtrarHechosPorDTO(List<Hecho> hechos, FiltroHechoDTO filtro) {
        return hechos.stream()
                // Título
                .filter(hecho -> filtro.getTitulo() == null
                        || (hecho.getTitulo() != null && hecho.getTitulo().toLowerCase().contains(filtro.getTitulo().toLowerCase()))
                )
                // Descripción
                .filter(hecho -> filtro.getDescripcion() == null
                        || (hecho.getDescripcion() != null && hecho.getDescripcion().toLowerCase().contains(filtro.getDescripcion().toLowerCase()))
                )
                // Etiqueta (al menos una etiqueta con ese nombre)
                .filter(hecho -> filtro.getEtiqueta() == null
                                || (hecho.getEtiquetas() != null
                                && hecho.getEtiquetas().stream()
                                .anyMatch(et -> et != null
                                        && et.getNombre() != null
                                        && et.getNombre().equalsIgnoreCase(filtro.getEtiqueta())
                                )
                        )
                )
                // Categoría (compara por nombre)
                .filter(hecho -> filtro.getCategoria() == null
                                || (hecho.getCategoria() != null
                                && hecho.getCategoria().getNombre() != null
                                && hecho.getCategoria().getNombre().equalsIgnoreCase(filtro.getCategoria())
                        )
                )
                // Provincia: ADAPTÁ ESTE FILTRO a cómo representás provincia/localidad en tu entity
                // Por ejemplo, si usás hecho.getUbicacion().getProvincia(), filtrá ahí.
                // Si no, podés comentar esta línea.
                // .filter(hecho -> filtro.getProvincia() == null
                //     || (hecho.getUbicacion() != null
                //         && hecho.getUbicacion().getProvincia() != null
                //         && hecho.getUbicacion().getProvincia().equalsIgnoreCase(filtro.getProvincia())
                //     )
                // )
                // Solo hechos con archivos/imágenes
                .filter(hecho -> filtro.getSoloMultimedia() == null
                        || !filtro.getSoloMultimedia()
                        || (hecho.getMultimedia() != null && !hecho.getMultimedia().isEmpty())
                )
                // Filtrado por fecha de suceso
                .filter(hecho -> filtro.getFechaDesdeSuceso() == null
                        || (hecho.getFechaSuceso() != null && !hecho.getFechaSuceso().isBefore(filtro.getFechaDesdeSuceso()))
                )
                .filter(hecho -> filtro.getFechaHastaSuceso() == null
                        || (hecho.getFechaSuceso() != null && !hecho.getFechaSuceso().isAfter(filtro.getFechaHastaSuceso()))
                )
                // Filtrado por fecha de carga
                .filter(hecho -> filtro.getFechaDesdeCarga() == null
                        || (hecho.getFechaCarga() != null && !hecho.getFechaCarga().isBefore(filtro.getFechaDesdeCarga()))
                )
                .filter(hecho -> filtro.getFechaHastaCarga() == null
                        || (hecho.getFechaCarga() != null && !hecho.getFechaCarga().isAfter(filtro.getFechaHastaCarga()))
                )
                .collect(Collectors.toList());
    }
}
