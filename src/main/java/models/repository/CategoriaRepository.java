package models.repository;

import models.entities.colecciones.Coleccion;
import models.entities.hecho.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {

        private static volatile CategoriaRepository instance;

        public CategoriaRepository() {
            if (instance != null) {
                throw new RuntimeException("Usa getInstance() para obtener el Singleton");
            }
        }

        public static CategoriaRepository getInstance() {
            if (instance == null) {
                synchronized (CategoriaRepository.class) {
                    if (instance == null) {
                        instance = new CategoriaRepository();
                    }
                }
            }
            return instance;
        }

        private final List<Categoria> categorias = new ArrayList<>();

        public List<Categoria> obtenerTodas(){
            return categorias;
        }

        public void delete(Categoria c){
            categorias.remove(c);
        }

        public Categoria getColeccion(int id) {
            return categorias.stream()
                    .filter(h -> h.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
        public  void add(Categoria h){
            categorias.add(h);
        }

    }


