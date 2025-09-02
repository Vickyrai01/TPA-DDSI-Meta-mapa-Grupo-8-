package models.agregador;

import java.time.LocalDate;
import java.util.*;

import models.entities.colecciones.Coleccion;
import models.entities.colecciones.criterios.Criterio;
import models.entities.colecciones.criterios.FiltradorCriterios;
import models.entities.hecho.Categoria;
import models.entities.hecho.Hecho;
import api.dto.HechoAIntegrarDTO;
import models.entities.solicitud.DetectorDeSpam;
import models.normalizador.ComparadorHechos;
import models.normalizador.FactoryHecho;
import models.normalizador.NormalizadorCategoria;
import models.normalizador.NormalizadorFecha;
import models.repository.ColeccionesRepository;
import models.agregador.cargadores.CargadorDinamico;
import models.agregador.cargadores.CargadorEstatico;
import models.agregador.cargadores.CargadorFuente;
import models.agregador.cargadores.CargadorProxy;
import models.repository.HechosRepository;

public class ServicioDeAgregacion {
    private List<HechoAIntegrarDTO> hechoRevisionManual = new ArrayList<>();

    private List<HechoAIntegrarDTO> hechosAIntegrar = new ArrayList<>();
    private List<Hecho> hechosLimpios = new ArrayList<>();

    private ColeccionesRepository coleccionesRepository = ColeccionesRepository.getInstance();
    private HechosRepository hechoRepository = HechosRepository.getInstance();

    private FiltradorCriterios filtradorCriterios = FiltradorCriterios.getInstance();


    private CargadorDinamico cargadorDinamico = CargadorDinamico.getInstance();
    private CargadorProxy cargadorProxy = CargadorProxy.getInstance();
    private CargadorEstatico cargadorEstatico = CargadorEstatico.getInstance();
    private List<CargadorFuente> cargadoresFuentes = List.of(
            cargadorProxy,
            cargadorEstatico,
            cargadorDinamico
    );

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

    private void obtenerTodosLosHechosNuevos () {
        for (CargadorFuente cargador : cargadoresFuentes) {
                List<HechoAIntegrarDTO> lista = cargador.extraerHechosAIntegrar();  //extraerHechosRecientes();
                eliminarSpam(lista);
            //System.out.println("Pase eliminar spam");
                eliminarDuplicados(lista);
            //System.out.println("Pase eliminar duplicados");
                normalizadorCategoria.estandarizarCategoriasDuplicadas(lista);
            //System.out.println("Pase el normalizador");
                hechosAIntegrar.addAll(lista);
                //System.out.println("Hechos extraidos de la fuente: " + cargador.getClass().getSimpleName() + " " + lista.size() + "");
            }
    }

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
                hechoRevisionManual.add(dto);
             }
        }
    }

    private void agregarHechosAColecciones(Coleccion coleccion)
    {
            List<Criterio> criterios = coleccion.getCriterioDePertenencia();
            List<Hecho> hechosFiltrados = filtradorCriterios.filtrarHechos(hechosLimpios, criterios);
            for (Hecho hecho : hechosFiltrados) {
                hechoRepository.add(hecho);
                coleccion.agregarHecho(hecho);
            }
    }

    public void actualizarColecciones()
    {
        hechosAIntegrar.clear();
        hechosLimpios.clear();

        obtenerTodosLosHechosNuevos();
        System.out.println("Obtuve los hechos nuevos...");
        normalizarYCrearHechos();
        System.out.println("Hechos limpiados y normalizados....");

        for (Coleccion coleccion : coleccionesRepository.obtenerTodas()) {
            System.out.println(coleccion.toString());
            agregarHechosAColecciones(coleccion);
            System.out.println("**Agregue a colección**");
        }

        System.out.println("**Hay " + hechosLimpios.size() + " nuevos hechos extraidos**");
        System.out.println("**Hay " + hechoRepository.obtenerTodas().size() + " hechos en el repositorio**");
        hechosAIntegrar.clear();
        hechosLimpios.clear();
    }


}



