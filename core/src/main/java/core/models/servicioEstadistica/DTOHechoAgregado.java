package core.models.servicioEstadistica;

import java.time.LocalDateTime;

public class DTOHechoAgregado {
    String hash;
    String provincia;
    String categoria;
    String fecha_suceso;

    public DTOHechoAgregado()
    {}

    public DTOHechoAgregado(String hash, String provincia, String categoria, String dia) {
        this.hash = hash;
        this.provincia = provincia;
        this.categoria = categoria;
        this.fecha_suceso = dia;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public String toString() {
        return "HechoAgregado{" +
                "id_hecho='" + hash + '\'' +
                ", provincia='" + provincia + '\'' +
                ", categoria='" + categoria + '\'' +
                ", fecha_suceso='" + fecha_suceso + '\'' +
                '}';
    }
}
