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
        System.out.println("[DEBUG] Intentando registrar usuario: " + (usuario != null ? usuario.getCorreo() : "null"));
        if (usuario == null || usuario.getCorreo() == null) {
            System.out.println("[ERROR] Datos de usuario inválidos");
            ctx.status(400).result("Datos de usuario inválidos");
            return;
        }
        boolean existe = UsuarioRepository.getInstance().findByCorreo(usuario.getCorreo()).isPresent();
        System.out.println("[DEBUG] ¿Existe el usuario? " + existe);
        if (existe) {
            System.out.println("[ERROR] El correo ya está registrado");
            ctx.status(409).result("El correo ya está registrado");
        } else {
            try {
                UsuarioRepository.getInstance().add(usuario);
                System.out.println("[DEBUG] Usuario registrado correctamente: " + usuario.getCorreo());
                ctx.status(201).json(usuario);
            } catch (Exception e) {
                System.out.println("[ERROR] Fallo al registrar usuario: " + e.getMessage());
                ctx.status(500).result("Error al registrar usuario");
            }
        }
    };
}
