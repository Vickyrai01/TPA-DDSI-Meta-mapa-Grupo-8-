package cargadorEstatica.model;

public class FuenteDTO {
    private String nombre;
    private String link;
    private String tipoFuente;

    public FuenteDTO(String nombre, String link, String tipoFuente) {
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }
}
