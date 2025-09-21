package cargadorDinamica.repository;

import cargadorDinamica.model.HechoAIntegrarDTO;

import java.util.*;

public class DinamicaRepository {
    private static volatile DinamicaRepository instance;

    private List<Map<HechoAIntegrarDTO, Boolean>> tablaHechos =
            new ArrayList<>(Arrays.asList(new HashMap<>(), new HashMap<>()));


    private DinamicaRepository() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

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

    private static final List<HechoAIntegrarDTO> hechosDinamicos = new ArrayList<>();

    public List<HechoAIntegrarDTO> obtenerTodas() {
        return hechosDinamicos;
    }

    public void add(HechoAIntegrarDTO h) {
        Map<HechoAIntegrarDTO, Boolean> elemento = new HashMap<>();
        elemento.put(h, false);
        tablaHechos.add(elemento);
    }

    public void delete(HechoAIntegrarDTO h) {
        hechosDinamicos.remove(h);
    }

    public List<HechoAIntegrarDTO> getHechosNoProcesados() {
        List<HechoAIntegrarDTO> noProcesados = new ArrayList<>();

        for (Map<HechoAIntegrarDTO, Boolean> elemento : tablaHechos) {
            for (Map.Entry<HechoAIntegrarDTO, Boolean> entry : elemento.entrySet()) {
                if (!entry.getValue()) { // si es false
                    noProcesados.add(entry.getKey()); // agrego el hecho
                    entry.setValue(true);             // marco como procesado
                }
            }
        }

        return noProcesados;
    }
}
