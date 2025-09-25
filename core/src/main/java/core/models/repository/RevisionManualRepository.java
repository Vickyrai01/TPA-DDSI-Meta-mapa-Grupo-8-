package core.models.repository;

import core.api.DTO.HechoAIntegrarDTO;

import java.util.ArrayList;
import java.util.List;

public class RevisionManualRepository {

    private static volatile RevisionManualRepository instance;

    private RevisionManualRepository() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static RevisionManualRepository getInstance() {
        if (instance == null) {
            synchronized (RevisionManualRepository.class) {
                if (instance == null) {
                    instance = new RevisionManualRepository();
                }
            }
        }
        return instance;
    }

    private final List<HechoAIntegrarDTO> hechos = new ArrayList<>();

    public List<HechoAIntegrarDTO> obtenerTodas(){
        return hechos;
    }

    public void delete(HechoAIntegrarDTO h){
        hechos.remove(h);
    }

    public HechoAIntegrarDTO getHecho(String id) {
        return hechos.stream()
                .filter(h -> h.getHash() == id)
                .findFirst()
                .orElse(null);
    }

    public  void add(HechoAIntegrarDTO h){
        hechos.add(h);
    }

    public Boolean existeElHecho(String hash){
        if (hechos.stream().anyMatch(h -> h.getHash().equalsIgnoreCase(hash))){
            return true;
        }
        else {
            return false;
        }
    }

}
