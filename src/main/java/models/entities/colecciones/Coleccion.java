package models.entities.colecciones;

import models.entities.colecciones.criterios.Criterio;
import models.entities.fuentes.Fuente;
import models.entities.hecho.Hecho;

import java.util.ArrayList;
import java.util.List;
public class Coleccion {

    public Coleccion(int id, String titulo, String descripcionColeccion, List<Criterio> criterioDePertenencia, List<Fuente> fuentes, List<Hecho> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = criterioDePertenencia;
        this.fuentes = fuentes;
        this.hechos = hechos;
        this.hechosVisibles = new ArrayList<>();
        this.identificadorHandle = identificadorHandle;
        this.modoDeNavegacion=ModoDeNavegacion.IRRESTRICTO;
        this.algoritmoConsenso=null;
    }

    private int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    private String titulo;
    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo = titulo;}

    private List<Fuente> fuentes;
    public List<Fuente> getFuentes() {return this.fuentes;}
    public void setFuentes(List<Fuente> fuentes){this.fuentes = fuentes;}

    public void agregarFuente (Fuente f) {fuentes.add(f);}
    public void agregarFuentes (List<Fuente> listaFuentes){fuentes.addAll(listaFuentes);}
    public void eliminarFuente (Fuente f) {fuentes.remove(f);}

    public List<String> extraerCodigosDeFuentes(List<Fuente> fuentes){return fuentes.stream().map(Fuente::getCodigoDeFuente).toList();}

    private String descripcionColeccion;
    public String getDescripcionColeccion() {return descripcionColeccion;}
    public void setDescripcionColeccion(String descripcionColeccion) {this.descripcionColeccion = descripcionColeccion;}

    private List<Criterio> criterioDePertenencia;

    public ModoDeNavegacion modoDeNavegacion;

    public AlgoritmoConsenso algoritmoConsenso = null;
    public void cambiarAlgoritmoConsenso(TipoConsenso algoritmoConsenso){
      switch (algoritmoConsenso){
          case TipoConsenso.ABSOLUTO -> this.setAlgoritmoConsenso(new StrategyAbsoluta());
          case TipoConsenso.MAYORIA_SIMPLE -> this.setAlgoritmoConsenso(new StrategyMayoriaSimple());
          case TipoConsenso.MULTIPLES_MENCIONES -> this.setAlgoritmoConsenso(new StrategyMultiplesMenciones());
      }
    }

    public AlgoritmoConsenso getAlgoritmoConsenso() {return algoritmoConsenso;}
    public void setAlgoritmoConsenso(AlgoritmoConsenso algoritmoConsenso) {this.algoritmoConsenso = algoritmoConsenso;}

    public List<Hecho> hechosVisibles;
    public List<Hecho> getHechosVisibles(){return hechosVisibles;}

    public void actualizarColeccionVisible(List<Fuente> fuentes, List<Hecho> hechos){
        if(modoDeNavegacion == modoDeNavegacion.CURADA){
            if(algoritmoConsenso==null){
                hechosVisibles= hechos;
            }else{
                hechosVisibles= algoritmoConsenso.ejecutarAlgoritmo(fuentes,hechos);
            }
        }
        else{
            hechosVisibles=hechos;
        }
    }

    public void modificarModoNavegacion(ModoDeNavegacion modoDeNavegacion){
        this.modoDeNavegacion=modoDeNavegacion;
        actualizarColeccionVisible(fuentes, hechos);
    }

    public List<Criterio> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }
    public void setCriterioDePertenencia(List<Criterio> criterioDePertenencia) {this.criterioDePertenencia = criterioDePertenencia;}

    public void agregarCriterio(Criterio criterio) {criterioDePertenencia.add(criterio);};
    public void eliminarCriterio(Criterio criterio) {criterioDePertenencia.remove(criterio);};

    private List<Hecho> hechos;
    public List<Hecho> getHechos() {return hechos;}
    public void setHechos(List<Hecho> hechos) {this.hechos = hechos;}
    public void agregarHechosDeFuentes(List<Hecho> hechos){this.hechos.addAll(hechos.stream().filter(unHecho -> unHecho.perteneceAFuente(extraerCodigosDeFuentes(this.fuentes))).toList());}
    public void agregarHecho(Hecho hecho){this.hechos.add(hecho);}

    private String identificadorHandle;
    public String getIdentificadorHandle() {return identificadorHandle;}
    public void setIdentificadorHandle(String identificadorHandle) {this.identificadorHandle = identificadorHandle;}
}
