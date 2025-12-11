package core.api.handlers.usuario;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class GetUsuarioPorCorreoHandler implements Handler {
    UsuarioRepository usuarioRepository = UsuarioRepository.getInstance();

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
            String correo = ctx.queryParam("correo");

            if (correo == null) {
                ctx.status(400).result("Falta el parámetro correo");
                return;
            }

            Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

            if (usuario == null) {
                ctx.status(404).result("Usuario no encontrado");
            } else {
                ctx.status(200).json(usuario);
            }
        }
    }

