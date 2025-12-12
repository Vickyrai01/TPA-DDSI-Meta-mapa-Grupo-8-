package core.api.handlers.usuario;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class PostRegistrarUsuarioHandler implements Handler {

    private final UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) {

        Usuario usuario = ctx.bodyAsClass(Usuario.class);

        System.out.println("[DEBUG] Intentando registrar usuario: " +
                (usuario != null ? usuario.getCorreo() : "null"));

        if (usuario == null
                || usuario.getCorreo() == null
                || usuario.getNombre() == null) {

            System.out.println("[ERROR] Datos de usuario incompletos");
            ctx.status(400).result("Faltan datos obligatorios del usuario");
            return;
        }

        boolean existe = usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent();
        System.out.println("[DEBUG] ¿Existe el usuario? " + existe);

        if (existe) {
            ctx.status(409).result("El correo ya está registrado");
            return;
        }

        try {
            usuarioRepository.add(usuario);
            System.out.println("[DEBUG] Usuario registrado correctamente: " + usuario.getCorreo());
            ctx.status(201).json(usuario);
        } catch (Exception e) {
            System.out.println("[ERROR] Fallo al registrar usuario: " + e.getMessage());
            ctx.status(500).result("Error al registrar usuario");
        }
    }
}
