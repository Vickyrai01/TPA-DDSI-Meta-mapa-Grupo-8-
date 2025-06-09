package models.entities.hecho;

import java.util.List;

public class ValidadorDeEtiqueta {

    public boolean noMasDeUnHorario(List<Etiqueta> etiquetas){
        return contarEtiquetasPorTipo(etiquetas, "Horario") <= 1;
    }
    public boolean noMasDeUnLugar(List<Etiqueta> etiquetas){
        return contarEtiquetasPorTipo(etiquetas, "Lugar") <= 1;
    }

    public long contarEtiquetasPorTipo(List<Etiqueta> etiquetas, String tipo){
        return etiquetas.stream().filter(unaEtiqueta -> unaEtiqueta.esEtiqueta(tipo)).count();
    }//se podria dejar solo esta funcion en vez de tener las otras dos de arriba, pero bueno por ahora lo dejo despues lo sacamos cualquier cosa

    public boolean tieneUnoDeCadaTipo(List<Etiqueta> etiquetas) {
        return tieneEtiquetaDeTipo(etiquetas, "Horario") &&
                tieneEtiquetaDeTipo(etiquetas, "Lugar") &&
                tieneEtiquetaDeTipo(etiquetas, "Fecha");
    }

    private boolean tieneEtiquetaDeTipo(List<Etiqueta> etiquetas, String tipo) {
        return etiquetas.stream().anyMatch(etiqueta -> etiqueta.esEtiqueta(tipo));
    }

}
