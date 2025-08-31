package models.repository;

import models.entities.colecciones.Coleccion;
import models.entities.fuentes.Fuente;
import models.entities.fuentes.TipoFuente;
import models.entities.normalizador.HechoAIntegrarDTO;

import java.util.List;
import java.util.ArrayList;

public class DinamicaRepository {

        private static volatile DinamicaRepository instance;

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

        private static final List<HechoAIntegrarDTO> hechos= new ArrayList<>();

        public List<HechoAIntegrarDTO> obtenerTodas(){
            return hechos;
        }

        public  void add(HechoAIntegrarDTO h){
            hechos.add(h);
        }

    }