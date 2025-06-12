package seeders;

import models.entities.hecho.Hecho;
import models.repository.HechosRepository;

public class seederHechos {

    public static void seederDB(){

        HechosRepository repoHechos = new HechosRepository();
        for (int i = 1; i < 10; i++){
            Hecho h = new Hecho();
            h.setId(i);
            h.setTitulo("Hecho " + i);
            h.setDescripcion("Descripcion del hecho " + i);

            repoHechos.add(h);
        }
    }

}
