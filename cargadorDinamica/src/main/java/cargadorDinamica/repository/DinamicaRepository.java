package cargadorDinamica.repository;

import cargadorDinamica.model.HechoAIntegrarDTO;

import java.util.*;

public class DinamicaRepository {
    private static volatile DinamicaRepository instance;

    private List<Map<HechoAIntegrarDTO, Boolean>> tablaHechos =
            new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>()));


    public static DinamicaRepository getInstance() {
        if (instance == null) {
            synchronized (DinamicaRepository.class) {
                if (instance == null) {
                    instance = new DinamicaRepository();
                }
            }
        }
        return instance;
    }
    public void add(HechoAIntegrarDTO h) {
        Map<HechoAIntegrarDTO, Boolean> elemento = new HashMap<>();
        elemento.put(h, false);
        tablaHechos.add(elemento);
    }

    public List<HechoAIntegrarDTO> getHechosNoProcesados() {
        List<HechoAIntegrarDTO> noProcesados = new ArrayList<>();

        for (Map<HechoAIntegrarDTO, Boolean> elemento : tablaHechos) {
            for (Map.Entry<HechoAIntegrarDTO, Boolean> entry : elemento.entrySet()) {
                if (!entry.getValue()) {
                    noProcesados.add(entry.getKey());
                    entry.setValue(true);
                }
            }
        }

        return noProcesados;
    }
}
