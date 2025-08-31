package models.entities.normalizador;

import models.entities.hecho.Hecho;
import models.repository.HechosRepository;

import java.util.List;

public class Normalizador {

    HechosRepository hechosRepository = HechosRepository.getInstance();

    /*1. Problema del titulo:
    Comparar los titulos utilizando el REGEX- ELIMINA ARTICULOS PONE EN MINUSCULA.
    Coincidencia de atributos a un 80%.
    En caso de que coincidan y no sea de otra instancia de metaMapa, fecha del hecho mas cercana.
    Si es de otra instancia de metamapa se ve la de ultima modificacion, asi no pierde el trabajo que se hizo. */

    public List<Hecho> normalizarHechos(List<HechoAIntegrarDTO> hechos) {
        for (HechoAIntegrarDTO hecho : hechos) {
            //evaluarDuplicado(hecho, hechos);
        }
        return null;
    }
   /*

   public Hecho normalizarHecho(HechoAIntegrarDTO hecho){

   }*/
/*
   public String ponerEnMinuscula(String palabra){
       return palabra.toLowerCase();
   }*/

}



