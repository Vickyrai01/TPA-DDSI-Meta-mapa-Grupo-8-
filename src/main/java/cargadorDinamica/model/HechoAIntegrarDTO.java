package cargadorDinamica.model;

import java.util.List;

public class HechoAIntegrarDTO {

    public String Titulo ;
    public String Descripcion;
    public String Categoria;
    public String Latitud;
    public String Longitud;
    public String FechaSuceso;
    public List<String> Etiquetas;
    public String Contribuyente;
    public String Multimedia;
    public String TipoFuente;
    public Boolean FueExtraido;
    public Integer IdFuente;

    public void setTipoFuente(String tipoFuente) {
        TipoFuente = tipoFuente;
    }
}

