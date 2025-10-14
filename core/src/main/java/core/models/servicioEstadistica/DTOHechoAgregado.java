package core.models.servicioEstadistica;

import java.time.LocalDateTime;

public class DTOHechoAgregado {
    String id_hecho;
    String provincia;
    String categoria;
    String fecha_suceso;

    public DTOHechoAgregado()
    {}

    public DTOHechoAgregado(String id_hecho, String provincia, String categoria, String dia) {
        this.id_hecho = id_hecho;
        this.provincia = provincia;
        this.categoria = categoria;
        this.fecha_suceso = dia;
    }

    public String getId_hecho() {
        return id_hecho;
    }

    public void setId_hecho(String id_hecho) {
        this.id_hecho = id_hecho;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDia() {
        return fecha_suceso;
    }

    public void setDia(String dia) {
        this.fecha_suceso = dia;
    }
}
