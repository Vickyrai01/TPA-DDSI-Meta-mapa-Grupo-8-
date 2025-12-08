package core.api.handlers.usuario;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class UsuarioApiHandler {
    public static Handler buscarPorCorreo = ctx -> {
        String correo = ctx.queryParam("correo");
        if (correo == null) {
            ctx.status(400).result("Falta el parámetro correo");
            return;
        }
        Usuario usuario = UsuarioRepository.getInstance().findByCorreo(correo).orElse(null);
        if (usuario == null) {
            ctx.status(404).result("Usuario no encontrado");
        } else {
            ctx.json(usuario);
        }
    };

    public static Handler registrar = ctx -> {
        Usuario usuario = ctx.bodyAsClass(Usuario.class);
        if (usuario == null || usuario.getCorreo() == null) {
            ctx.status(400).result("Datos de usuario inválidos");
            return;
        }
        boolean existe = UsuarioRepository.getInstance().findByCorreo(usuario.getCorreo()).isPresent();
        if (existe) {
            ctx.status(409).result("El correo ya está registrado");
        } else {
            UsuarioRepository.getInstance().add(usuario);
            ctx.status(201).json(usuario);
        }
    };
}
