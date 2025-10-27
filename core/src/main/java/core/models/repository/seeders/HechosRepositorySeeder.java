package core.models.repository.seeders;

import core.models.agregador.normalizador.NormalizadorCategoria;
import core.models.entities.colecciones.criterios.CriterioNombre;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.*;
import core.models.repository.CategoriaRepository;
import core.models.repository.ContribuyentesRepository;
import core.models.repository.CoordenadasRepository;
import core.models.repository.HechosRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HexFormat;

public class HechosRepositorySeeder {

    private static volatile HechosRepositorySeeder instance;

    public static HechosRepositorySeeder getInstance() {
        if (instance == null) {
            synchronized (HechosRepositorySeeder.class) {
                if (instance == null) {
                    instance = new HechosRepositorySeeder();
                }
            }
        }
        return instance;
    }

    Coordenadas coordenadas1 = new Coordenadas(123.0, 456.0);
    Coordenadas coordenadas2 = new Coordenadas(893.0, 016.0);
    Coordenadas coordenadas3 = new Coordenadas(973.0, 656.0);
    Coordenadas coordenadas4 = new Coordenadas(223.0, 033.0);

    HechosRepository hechosRepository = HechosRepository.getInstance();
    CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    CoordenadasRepository coordenadasRepository = CoordenadasRepository.getInstance();
    ContribuyentesRepository contribuyentesRepository = ContribuyentesRepository.getInstance();

    public static String generarHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return HexFormat.of().formatHex(hashBytes); // devuelve un string en hex
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarHechosSeeder() {

        Categoria categoriaIncendio = new Categoria("Incendio");
        Categoria categoriaChoque = new Categoria("Choque");
        Categoria categoriaRobo = new Categoria("Robo");

        categoriaRepository.add(categoriaIncendio);
        categoriaRepository.add(categoriaChoque);
        categoriaRepository.add(categoriaRobo);

        Coordenadas coordenadas1 = new Coordenadas(-34.61969316128249, -58.4494697301956);
        Coordenadas coordenadas2 = new Coordenadas(-34.55289162117985, -58.697023577203126);
        Coordenadas coordenadas3 = new Coordenadas(-34.63254438302316, -58.36621806522921);
        Coordenadas coordenadas4 = new Coordenadas(-34.60651289899796, -58.43499919363226);
        Coordenadas coordenadas5 = new Coordenadas(-34.71314181885889, -58.40695807398643);

        coordenadasRepository.add(coordenadas1);
        coordenadasRepository.add(coordenadas2);
        coordenadasRepository.add(coordenadas3);
        coordenadasRepository.add(coordenadas4);
        coordenadasRepository.add(coordenadas5);

        Contribuyente contribuyente1 = new Contribuyente("Mariana","Rossi",LocalDate.now().minusDays(23));
        Contribuyente contribuyente2 = new Contribuyente("Juan","Navia",LocalDate.now().minusDays(23));
        Contribuyente contribuyente3 = new Contribuyente("Luciana","Jofre",LocalDate.now().minusDays(23));

        contribuyentesRepository.add(contribuyente2);
        contribuyentesRepository.add(contribuyente1);
        contribuyentesRepository.add(contribuyente3);

        Hecho hecho1 = new Hecho(coordenadas1, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, contribuyente2,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "No hubo heridos, fue por una sartén", "Incendio en casa", "C1",1);
        String hash1 = generarHash(hecho1.getTitulo()+hecho1.getDescripcion()+hecho1.getCategoria()+hecho1.getUbicacion().getLatitud()+hecho1.getUbicacion().getLongitud());
        hecho1.setHash(hash1);
        Hecho hecho2 = new Hecho(coordenadas2, categoriaChoque, null,
                null, null, Estado.ACEPTADO, contribuyente3,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(3),
                TipoFuente.ESTATICA, null, "Un perro cruzo por la calle y frenó de golpe, todos a salvo.", "Choque entre moto y gol", "C1",1);
        String hash2 = generarHash(hecho2.getTitulo()+hecho2.getDescripcion()+hecho2.getCategoria()+hecho2.getUbicacion().getLatitud()+hecho2.getUbicacion().getLongitud());
        hecho2.setHash(hash2);
        Hecho hecho3 = new Hecho(coordenadas5, categoriaIncendio, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null, "Causa desconocida", "Departamento en un edicio", "C3",1);
        String hash3 = generarHash(hecho3.getTitulo()+hecho3.getDescripcion()+hecho3.getCategoria()+hecho3.getUbicacion().getLatitud()+hecho3.getUbicacion().getLongitud());
        hecho3.setHash(hash3);
        Hecho hecho4 = new Hecho(coordenadas3, categoriaChoque, null,
                null, null, Estado.ACEPTADO, contribuyente1,
                LocalDate.now().minusDays(1), LocalDate.now().minusDays(2),
                TipoFuente.ESTATICA, null, "Parecía que el conductor iba borracho, se llevó puesto una maceta que estaba en la calle", "Choque con maceta", "C2",1);
        String hash4 = generarHash(hecho4.getTitulo()+hecho4.getDescripcion()+hecho4.getCategoria()+hecho4.getUbicacion().getLatitud()+hecho4.getUbicacion().getLongitud());
        hecho4.setHash(hash4);
        Hecho hecho5 = new Hecho( coordenadas4, categoriaRobo, null,
                null, null, Estado.ACEPTADO, null,
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(6),
                TipoFuente.ESTATICA, null, "Se robó unas manzanas y bolsas", "Hurto en una verdulería", "C4",1);
        String hash5 = generarHash(hecho5.getTitulo()+hecho5.getDescripcion()+hecho5.getCategoria()+hecho5.getUbicacion().getLatitud()+hecho5.getUbicacion().getLongitud());
        hecho5.setHash(hash5);
        hechosRepository.add(hecho1);
        hechosRepository.add(hecho2);
        hechosRepository.add(hecho3);
        hechosRepository.add(hecho4);
        hechosRepository.add(hecho5);
    }
}