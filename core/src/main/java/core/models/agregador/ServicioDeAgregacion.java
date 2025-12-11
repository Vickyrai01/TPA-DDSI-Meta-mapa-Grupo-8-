package core.models.agregador;

import core.models.agregador.normalizador.*;
import core.models.entities.colecciones.Coleccion;
import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.colecciones.criterios.FiltradorCriterios;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.*;
import core.models.repository.ColeccionesRepository;
import core.models.repository.HechosRepository;
import core.models.repository.RevisionManualRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ServicioDeAgregacion {
    private RevisionManualRepository revisionManualRepository = RevisionManualRepository.getInstance();

    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();

    private ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private HechosRepository hechosRepository = HechosRepository.getInstance();

    private FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();

    private DetectorDeSpam detectorDeSpam = DetectorDeSpam.getInstance();
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();
    private NormalizadorFecha normalizadorFecha = NormalizadorFecha.getInstance();
    private NormalizadorCategoria normalizadorCategoria = NormalizadorCategoria.getInstance();
    private NormalizadorCoordenada normalizadorCoordenada = NormalizadorCoordenada.getInstance();
    private NormalizadorEtiqueta normalizadorEtiqueta = NormalizadorEtiqueta.getInstance();
    private NormalizadorContribuyente normalizadorContribuyente = NormalizadorContribuyente.getInstance();
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

    private void eliminarSpam(List <HechoAIntegrarDTO> lista) {
        if (lista == null || lista.isEmpty()) return;

        Iterator<HechoAIntegrarDTO> it = lista.iterator();

        while (it.hasNext()) {
            HechoAIntegrarDTO h = it.next();

            boolean tituloSpam = detectorDeSpam.esSpam(h.getTitulo());
            boolean descripcionSpam = detectorDeSpam.esSpam(h.getDescripcion());

            if (tituloSpam || descripcionSpam) {
                System.out.println(
                        "[SPAM] Eliminando hecho con hash=" + h.getHash()
                                + " | titulo=\"" + h.getTitulo() + "\""
                                + " | descripcion=\"" + h.getDescripcion() + "\""
                );
                it.remove();
            }
        }
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

        System.out.println("antes de normalizar hechos: " + hechosAIntegrar.size());
        for (HechoAIntegrarDTO dto : hechosAIntegrar) {
            try{
                Categoria categoria = normalizadorCategoria.obtenerCategoria(dto.getCategoria()); //Solo la crea
                LocalDate fecha = normalizadorFecha.normalizarFecha(dto.getFechaSuceso()); // hace el quilombo de fecha
                Coordenadas ubicacion = normalizadorCoordenada.obtenerCoordenadas(dto.getLatitud(), dto.getLongitud()); // solo la crea
                List<Etiqueta> etiquetas = normalizadorEtiqueta.obtenerEtiquetas(dto.getEtiquetas());
                Contribuyente contribuyente = normalizadorContribuyente.obtenerContribuyente(dto.getContribuyente());
                Hecho hecho = factoryHecho.convertirHecho(dto, fecha, categoria, ubicacion, etiquetas, contribuyente); // factory que funciona
                hechosLimpios.add(hecho);
            } catch (NormalizadorFecha.ExcepcionRevisionManualFecha e) {
                System.out.println("A revisión manual");
             }
        }
    }

    private void agregarHechosAColecciones(Integer idColeccion)
    {
        Coleccion coleccion = coleccionesRepository.findByIdConCriterios(idColeccion);
        List<Criterio> criterios = coleccion.getCriterioDePertenencia();

        // 2) Traigo solo los IDs de las fuentes de esa colección
        List<Integer> idsFuentesDeColeccion = coleccionesRepository.obtenerIdsFuentesDeColeccion(idColeccion);

        List<Hecho> hechosFiltradosFuentes = hechosRepository.obtenerHechosPorIdsFuente(idsFuentesDeColeccion);
        List<Hecho> hechosFiltradosCriterio = filtradorCriterios.filtrarHechos(hechosFiltradosFuentes, criterios);

        List<Integer> idHechos = hechosFiltradosCriterio.stream().map(Hecho::getId).toList();
        coleccionesRepository.agregarHechosAColeccion(idColeccion, idHechos);
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
        System.out.println("Cantidad de hechos a agregados a integrar: " + hechosAIntegrar.size());
        limpiarHechos();
        System.out.println("Cantidad de hechos limpiados: " + hechosAIntegrar.size());
        normalizarYCrearHechos();
        hechosRepository.addAllEnUnaTransaccion(hechosLimpios);
        List<Coleccion> colecciones = coleccionesRepository.obtenerTodas();
        System.out.println("Obtuve todas las colecciones.." + " son " + colecciones.size() + " colecciones.");
        for (Coleccion coleccion : colecciones) {
            agregarHechosAColecciones(coleccion.getId());
        }
        hechosAIntegrar.clear();
        hechosLimpios.clear();
        colecciones.clear();
    }

    public void hechoUnicoUrgente(HechoAIntegrarDTO hechoUnico) {
        if (hechoUnico == null) return;
        System.out.println(hechoUnico.getHash());
        actualizarColecciones(List.of(hechoUnico));
    }

}



