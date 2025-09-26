package core.models.agregador;

import core.api.DTO.HechoAIntegrarDTO;
import core.models.agregador.normalizador.ComparadorHechos;
import core.models.agregador.normalizador.FactoryHecho;
import core.models.agregador.normalizador.NormalizadorCategoria;
import core.models.agregador.normalizador.NormalizadorFecha;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Hecho;
import core.models.repository.ColeccionesRepository;
import core.models.repository.HechosRepository;
import core.models.repository.RevisionManualRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServicioDeAgregacion {
    private RevisionManualRepository revisionManualRepository = RevisionManualRepository.getInstance();

    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();

    private ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private HechosRepository hechoRepository = HechosRepository.getInstance();

    private FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();

    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();
    private NormalizadorFecha normalizadorFecha = NormalizadorFecha.getInstance();
    private NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    private FactoryHecho factoryHecho = FactoryHecho.getInstance();

    private static volatile ServicioDeAgregacion instance;

    private ServicioDeAgregacion() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static ServicioDeAgregacion getInstance() {
        if (instance == null) {
            synchronized (ServicioDeAgregacion.class) {
                if (instance == null) {
                    instance = new ServicioDeAgregacion();
                }
            }
        }
        return instance;
    }

    //FLUJO:
    //1.  Obtenemos todos los HechosDTO a integrar de las fuentes, eliminando duplicados fuente a fuente. Pensar un algoritmo.
    //2.  Eliminamos los spam
    //3.  Por cada hecho a integrar verifique los duplicados contra la lista de hechosAIntegrar.
    //    - En caso de haber una coincidencia...elegimos una categoria para ponerle!
    //    - Cranear un poco mas lo de la categoria, onda cual tomamos. -> NormalizadorCategoria
    // 4. Normalizar la fecha
    //  - 4.1 si no se puede normalizar se manda a revisión manual
    // 5. Enviar al Factory para crear el hecho
    // 6. Agregar a las colecciones correspondientes (ver lo de los criterios de pertenencia)

    private void eliminarSpam(List <HechoAIntegrarDTO> lista){
        lista.removeIf(h -> DetectorDeSpam.esSpam(h.getTitulo()) || DetectorDeSpam.esSpam(h.getDescripcion()));
        }


    public void eliminarDuplicados(List<HechoAIntegrarDTO> hechos) {
        //System.out.print(hechos);
        Objects.requireNonNull(hechos, "lista nula");
        if (hechos.isEmpty() || hechos.size() == 1) return;
        for (int i = 0; i < hechos.size(); i++) {
            HechoAIntegrarDTO hi = hechos.get(i);
            for (int j = i + 1; j < hechos.size(); ) {
                if (comparadorHechos.hechoDuplicado(hi, hechos.get(j))) {
                    hechos.remove(j);
                } else {
                    j++; // solo avanzá si no eliminaste
                }
            }
        }
    }

    //1. Buscar los hechos parecidos, varios grupos de hechos parecidos
    //2. Dejamos una lista para los no parecidos
    //3. NormalizadorCategoria: 1 que normaliza normal, la busca en el repo
    //     NormalizadorCategroria que reciba una lista y haga la logica
    //     Si crea una categoria nueva y es solo, se deja o se manda a revisión??

    public void normalizarYCrearHechos() {
        for (HechoAIntegrarDTO dto : hechosAIntegrar) {
            try{
                Categoria categoria = normalizadorCategoria.obtenerCategoria(dto.getCategoria());
                LocalDate fecha = normalizadorFecha.normalizarFecha(dto.getFechaSuceso());
                Hecho hecho = factoryHecho.convertirHecho(dto, fecha, categoria);
                hechosLimpios.add(hecho);
            } catch (NormalizadorFecha.ExcepcionRevisionManualFecha e) {
                //Enviar a revisión manual
                revisionManualRepository.add(dto);
                System.out.println("A revisión manual");
             }
        }
    }

    private void agregarHechosAColecciones(Coleccion coleccion)
    {
            List<Criterio> criterios = coleccion.getCriterioDePertenencia();
            List<Integer> linkFuentesDeColeccion = coleccion.getFuentes().stream()
                .map(f -> f.getId())
                .toList();
            List<Hecho> hechosFiltradosFuentes = hechosLimpios.stream().filter(h -> linkFuentesDeColeccion.contains(h.getIdFuente())).toList();
            List<Hecho> hechosFiltradosCriterio = filtradorCriterios.filtrarHechos(hechosFiltradosFuentes, criterios);
            for (Hecho hecho : hechosFiltradosCriterio) {
                hechoRepository.add(hecho);
                coleccion.agregarHecho(hecho);
            }
    }

    public void limpiarHechos() {
         eliminarSpam(hechosAIntegrar);
         eliminarDuplicados(hechosAIntegrar);
         normalizadorCategoria.estandarizarCategoriasDuplicadas(hechosAIntegrar);

    }

    //EL QUE SE USA!!
    public void actualizarColecciones(List<HechoAIntegrarDTO> lista){

        hechosAIntegrar.clear();
        hechosLimpios.clear();

        System.out.println("Cantidad de hechos a limpiar: " + lista.size());
        hechosAIntegrar.addAll(lista);
        limpiarHechos();
        normalizarYCrearHechos();
        System.out.println("Cantidad de hechos a agregar a coleccion: " + hechosLimpios.size());
        for (Coleccion coleccion : coleccionesRepository.obtenerTodas()) {
            agregarHechosAColecciones(coleccion);
            System.out.println("Agregar a colección " + " '" + coleccion.getTitulo() + "' " + " fue exitoso");
        }

        hechosAIntegrar.clear();
        hechosLimpios.clear();
    }
}



