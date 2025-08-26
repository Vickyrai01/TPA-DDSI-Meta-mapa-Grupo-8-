package models.entities.normalizador;

import models.entities.hecho.Categoria;
import models.repository.CategoriaRepository;

import java.util.List;
import java.util.Objects;

public class NormalizadorCategoria {

    private static NormalizadorCategoria instance;
    private CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();

    public static NormalizadorCategoria getInstance() {
        if (instance == null) instance = new NormalizadorCategoria();

        return instance;
    }


    public Categoria obtenerCategoria(String categoria) {
        String categoriaBase = categoria.trim().toLowerCase();

        if (categoriaRepository.existe(categoria)) {
            return categoriaRepository.buscarPorNombre(categoria);
        }

        if (categoriaBase.endsWith("s")) {
            String singular = categoriaBase.substring(0, categoriaBase.length() - 1);
            if (categoriaRepository.existe(singular)) {
                return categoriaRepository.buscarPorNombre(singular);
            } else {
                String plural = categoriaBase + "s";
                if (categoriaRepository.existe(plural)) {
                    return categoriaRepository.buscarPorNombre(plural);
                }
            }
        }

        Categoria categoriaNueva = new Categoria(categoria);
        categoriaRepository.add(categoriaNueva);
        return categoriaNueva;
    }

    public void estandarizarCategoriasDuplicadas(List<HechoAIntegrarDTO> hechos) {
        Objects.requireNonNull(hechos, "lista nula");
        for (int i = 0; i < hechos.size(); i++) {
            HechoAIntegrarDTO hi = hechos.get(i);
            for (int j = i + 1; j < hechos.size(); ) {
                HechoAIntegrarDTO hj = hechos.get(j);
                if (tienenDistintaCategoria(hi,hj) && comparadorHechos.esElMismoHecho(hi, hj)) {
                    hj.setCategoria(hi.getCategoria());
                } else {
                    j++; // solo avanzá si no eliminaste
                }
            }
        }
    }

    public Boolean tienenDistintaCategoria(HechoAIntegrarDTO hecho1, HechoAIntegrarDTO hecho2) {
        return !hecho1.getCategoria().equals(hecho2.getCategoria());
    }

}
