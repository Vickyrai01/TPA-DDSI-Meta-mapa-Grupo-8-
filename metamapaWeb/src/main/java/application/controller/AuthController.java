package application.controller;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena) {
        Optional<Usuario> usuarioOpt = UsuarioRepository.getInstance().findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasena().equals(contrasena)) {
                // Aquí podrías devolver un token o los datos del usuario
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(401).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("nombre") String nombre,
                                      @RequestParam("apellido") String apellido,
                                      @RequestParam("correo") String correo,
                                      @RequestParam("contrasena") String contrasena) {
        Optional<Usuario> usuarioOpt = UsuarioRepository.getInstance().findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            return ResponseEntity.status(409).body("El correo ya está registrado");
        }
        Usuario nuevo = new Usuario(nombre, apellido, correo, "USER", null, contrasena);
        UsuarioRepository.getInstance().add(nuevo);
        return ResponseEntity.ok(nuevo);
    }
}
