package core.models.agregador;

import core.api.DTO.HechoAIntegrarDTO;
import core.models.agregador.SchedulerAgregador;

import java.util.List;

public class DemoObtenerDeCargadores {
    public static void main(String[] args){
        SchedulerAgregador scheduler = new SchedulerAgregador(false);

       List<HechoAIntegrarDTO> hechoAIntegrarDTOList = scheduler.obtenerHechosAIntegrar();
       if(hechoAIntegrarDTOList.isEmpty()){System.out.println("Sin hechos.");}
        for (HechoAIntegrarDTO h : hechoAIntegrarDTOList) {
            System.out.println(h.toString());
        }
        System.out.println("========================================");
    }
    }
