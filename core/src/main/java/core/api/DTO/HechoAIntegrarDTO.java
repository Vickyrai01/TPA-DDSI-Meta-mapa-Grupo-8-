package core.api.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import core.models.agregador.HandlerRecientes;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)              // ignora campos extra en el JSON
@JsonInclude(JsonInclude.Include.NON_NULL)               // no serializa campos null en las respuestas
public class HechoAIntegrarDTO {

    @JsonProperty(access = Access.READ_ONLY)             // no se espera en el JSON de entrada
    public String hash;

    public String titulo;
    public String descripcion;
    public String categoria;
    public String latitud;
    public String longitud;
    public String fechaSuceso;

    public List<String> etiquetas;
    public String contribuyente;
    public List<String> multimedia; //A CHEQUEAR !!!!
    public String tipoFuente;

    @JsonProperty(access = Access.READ_ONLY)             // flag interno; no lo pidas en el request
    public Boolean fueExtraido;

    public Integer idFuente;

    public HechoAIntegrarDTO() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public HechoAIntegrarDTO(
            @JsonProperty(value = "titulo",       required = true) String titulo,
            @JsonProperty(value = "descripcion",  required = true) String descripcion,
            @JsonProperty(value = "categoria",    required = true) String categoria,
            @JsonProperty(value = "latitud",      required = true) String latitud,
            @JsonProperty(value = "longitud",     required = true) String longitud,
            @JsonProperty(value = "fechaSuceso",  required = true) String fechaSuceso
    ) {
        this.hash = HandlerRecientes.generarHash(titulo+descripcion+categoria+latitud+longitud);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;

        // opcionales: quedan null si no vienen
        this.etiquetas = null;
        this.contribuyente = null;
        this.multimedia = null;
        this.tipoFuente = null;

        this.fueExtraido = Boolean.FALSE; // default interno
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaSuceso,
                             List<String> etiquetas, String contribuyente, List<String> multimedia)
    {
        this.hash = HandlerRecientes.generarHash(titulo+descripcion+categoria+latitud+longitud);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.multimedia = multimedia;
        this.tipoFuente = null;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }
    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }
    public String getFechaSuceso() { return fechaSuceso; }
    public void setFechaSuceso(String fechaSuceso) { this.fechaSuceso = fechaSuceso; }
    public String getHash() { return hash; }
    public String getTipoFuente() {return tipoFuente;}
    public void setTipoFuente(String fuente) {this.tipoFuente = fuente;};
    public Integer getIdFuente() {return idFuente;}
    public void setIdFuente(Integer idFuente) {this.idFuente = idFuente;};


    public Boolean tieneMismoTitulo(String tituloExterno){
        String tituloPropioLimpio = this.getTitulo().toLowerCase().replace(" ","");
        String tituloExternoLimpio = tituloExterno.toLowerCase().replace(" ","");
        //Los pone en minusculas y le elimina los espacios
        return tituloPropioLimpio.equals(tituloExternoLimpio);
    }

    @Override
    public String toString() {
        return "HechoAIntegrarDTO{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", fechaSuceso='" + fechaSuceso + '\'' +
                ", etiquetas=" + etiquetas +
                ", contribuyente='" + contribuyente + '\'' +
                ", multimedia=" + multimedia +
                '}';
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria,
                             String latitud, String longitud, String fechaSuceso,
                             List<String> etiquetas, String contribuyente,
                             String multimedia, String tipoFuente,
                             Boolean fueExtraido, Integer idFuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.multimedia = Collections.singletonList(multimedia);
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.tipoFuente = tipoFuente;
        this.fueExtraido = fueExtraido;
        this.idFuente = idFuente;
    }

}
