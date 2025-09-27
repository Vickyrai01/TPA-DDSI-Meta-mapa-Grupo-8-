package core.models.entities.colecciones;

import core.models.entities.colecciones.criterios.Criterio;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="coleccion")
public class Coleccion {


    public Coleccion(Integer id, String titulo, String descripcionColeccion,
                     List<Criterio> criterioDePertenencia,
                     List<Fuente> fuentes,
                     List<Hecho> hechos,
                     String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia != null ? new ArrayList<>(criterioDePertenencia) : new ArrayList<>();
        this.fuentes = fuentes != null ? new ArrayList<>(fuentes) : new ArrayList<>();
        this.hechos = hechos != null ? new ArrayList<>(hechos) : new ArrayList<>();
        this.identificadorHandle = identificadorHandle;
        this.modoDeNavegacion = ModoDeNavegacion.IRRESTRICTO;
        this.algoritmoConsenso = null;
    }

    public Coleccion(){}

    @Id
    @Column(name = "id_coleccion")
    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    @Column(name = "titulo")
    private String titulo;
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    @Column(name = "fuentes")
    @ManyToMany
    @JoinColumn(name = "id_fuente")
    private List<Fuente> fuentes;
    public List<Fuente> getFuentes() {return this.fuentes;}
    public void setFuentes(List<Fuente> fuentes){this.fuentes = fuentes;}

    public void agregarFuente (Fuente f) {fuentes.add(f);}
    public void agregarFuentes (List<Fuente> listaFuentes){fuentes.addAll(listaFuentes);}
    public void eliminarFuente (Fuente f) {fuentes.remove(f);}

    public List<String> extraerCodigosDeFuentes(List<Fuente> fuentes){return fuentes.stream().map(Fuente::getCodigoDeFuente).toList();}

    @Column(name = "descripcionColeccion")
    private String descripcionColeccion;
    public String getDescripcionColeccion() {return descripcionColeccion;}
    public void setDescripcionColeccion(String descripcionColeccion) {this.descripcionColeccion = descripcionColeccion;}

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coleccion_id") // FK en la tabla de Criterio
    private List<Criterio> criterioDePertenencia;
    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }
    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {this.criterioDePertenencia = criterioDePertenencia;}
    public void agregarCriterio(Criterio criterio) {criterioDePertenencia.add(criterio);};
    public void eliminarCriterio(Criterio criterio) {criterioDePertenencia.remove(criterio);};

    @Column(name = "modoDeNavegacion")
    public ModoDeNavegacion modoDeNavegacion;

    @Convert(converter = AlgoritmoConsensoConverter.class)
    @Column(name = "strategy_tipo_conexion")
    public AlgoritmoConsenso algoritmoConsenso = null;
    public void cambiarAlgoritmoConsenso(TipoConsenso algoritmoConsenso){
      switch (algoritmoConsenso){
          case ABSOLUTO -> this.setAlgoritmoConsenso(new StrategyAbsoluta());
          case MAYORIA_SIMPLE -> this.setAlgoritmoConsenso(new StrategyMayoriaSimple());
          case MULTIPLES_MENCIONES -> this.setAlgoritmoConsenso(new StrategyMultiplesMenciones());
       }
    }
    public AlgoritmoConsenso getAlgoritmoConsenso() {return algoritmoConsenso;}
    public void setAlgoritmoConsenso(AlgoritmoConsenso algoritmoConsenso) {this.algoritmoConsenso = algoritmoConsenso;}



    @Column(name = "tipoConsenso")
    public TipoConsenso tipoConsenso;

    public List<Hecho> getHechosVisibles(){
        if(modoDeNavegacion == modoDeNavegacion.CURADA){
            if(algoritmoConsenso==null){
                return hechos;
            }else{
                return algoritmoConsenso.ejecutarAlgoritmo();
            }
        }
        else{
            return hechos;
        }

    }

    public void modificarModoNavegacion(ModoDeNavegacion modoDeNavegacion){
        this.modoDeNavegacion=modoDeNavegacion;
    }

    @Column(name = "hechos")
    @OneToMany
    @JoinColumn(name = "id_hecho")
    private List<Hecho> hechos;
    public List<Hecho> getHechos() {return hechos;}
    public void setHechos(List<Hecho> hechos) {this.hechos = hechos;}
    public void agregarHechosDeFuentes(List<Hecho> hechos){this.hechos.addAll(hechos.stream().filter(unHecho -> unHecho.perteneceAFuente(extraerCodigosDeFuentes(this.fuentes))).toList());}
    public void agregarHecho(Hecho hecho){this.hechos.add(hecho);}


    private String identificadorHandle;
    public String getIdentificadorHandle() {return identificadorHandle;}
    public void setIdentificadorHandle(String identificadorHandle) {this.identificadorHandle = identificadorHandle;}

    public boolean hechoYaExistenteEnColeccion(String hash)
    {return this.hechos.stream().anyMatch(h -> h.getHash().equals(hash));
    }

    @Override
    public String toString() {
        return titulo;
    }
}
