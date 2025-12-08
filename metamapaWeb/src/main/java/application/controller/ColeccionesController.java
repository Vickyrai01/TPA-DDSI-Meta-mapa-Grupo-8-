package application.controller;

import application.dto.ColeccionDTO;
import application.dto.HechoDTO;
import application.service.HechoService;
import core.api.DTO.HechoResumenDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import application.service.ColeccionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class ColeccionesController {
    private final ColeccionService coleccionService;
    private final HechoService hechosService;

    public ColeccionesController(ColeccionService coleccionService, HechoService hechosService) {
        this.coleccionService = coleccionService;
        this.hechosService = hechosService;
    }

    @GetMapping("/colecciones") // Esta es la URL que usará el botón
    public String navegarColecciones(Model model) {

        // (Opcional) Aquí podés buscar datos en tu 'core' y pasarlos a la vista
        model.addAttribute("listaDeColecciones", coleccionService.getAll());

        // Esto le dice a Thymeleaf que busque el archivo:
        // "src/main/resources/templates/navegarColecciones/navegarColecciones.html"
        return "navegarColecciones/navegarColecciones";
    }

    @GetMapping("/colecciones/{id}")
    public String detalle(
            @PathVariable("id") Integer id,
            @RequestParam(value = "modo", required = false, defaultValue = "CURADA") String modoParam,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "etiqueta", required = false) String etiqueta,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "fechaDesdeSuceso", required = false) String fechaDesdeSuceso,
            @RequestParam(value = "fechaHastaSuceso", required = false) String fechaHastaSuceso,
            @RequestParam(value = "fechaDesdeCarga", required = false) String fechaDesdeCarga,
            @RequestParam(value = "fechaHastaCarga", required = false) String fechaHastaCarga,
            @RequestParam(value = "provincia", required = false) String provincia,
            @RequestParam(value = "soloMultimedia", required = false) Boolean soloMultimedia,
            Model model
    ) {
        // Obtenés la colección
        ColeccionDTO coleccion = coleccionService.getById(id);

        // Acá llamás a un método de tu service que filtre por todos los parámetros
        List<HechoDTO> hechosFiltrados = hechosService.filtrarHechosDeColeccion(
                id, modoParam, titulo, descripcion, etiqueta, categoria,
                fechaDesdeSuceso, fechaHastaSuceso, fechaDesdeCarga, fechaHastaCarga, provincia, soloMultimedia
        );

        model.addAttribute("coleccion", coleccion);
        model.addAttribute("modoActual", modoParam);
        model.addAttribute("hechos", hechosFiltrados);

        model.addAttribute("categorias", hechosService.getCategorias());
        //model.addAttribute("etiquetas", hechosService.getEtiquetas()); arreglar porque no funciona esta poronga (hay que agregar el endpoint en apiMetaMapa)

        return "verColeccion/verColeccion";
    }

}
