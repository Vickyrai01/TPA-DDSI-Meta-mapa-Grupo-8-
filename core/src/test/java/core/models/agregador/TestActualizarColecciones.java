package core.models.agregador;

public class TestActualizarColecciones {

}


/*   //EL QUE SE USA!!
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
    }*/