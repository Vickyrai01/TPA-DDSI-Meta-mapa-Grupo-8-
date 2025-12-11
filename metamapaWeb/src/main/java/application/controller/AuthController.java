
package application.controller;

import application.service.RutasProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final WebClient metamapaApi;

    public AuthController(RutasProperties props) {
        this.metamapaApi = WebClient.create(props.getBaseUrl());
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena) {
        // Consumir API del core para buscar usuario por correo
        return metamapaApi.get()
                .uri(uriBuilder -> uriBuilder.path("/buscar").queryParam("correo", correo).build())
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .map(usuario -> {
                    if (usuario.getContrasena().equals(contrasena)) {
                        // Aquí podrías devolver un token o los datos del usuario
                        return ResponseEntity.ok(usuario);
                    } else {
                        return ResponseEntity.status(401).body("Contraseña incorrecta");
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(404).body("Usuario no encontrado"));
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestParam("nombre") String nombre,
                                                 @RequestParam("apellido") String apellido,
                                                 @RequestParam("correo") String correo,
                                                 @RequestParam("contrasena") String contrasena) {
        // Consumir API del core para verificar si el usuario existe
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return metamapaApi.get()
                .uri(uriBuilder -> uriBuilder.path("/buscar").queryParam("correo", correo).build())
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful() && response.headers().contentType().isPresent() &&
                            response.headers().contentType().get().toString().contains("json")) {
                        return response.bodyToMono(UsuarioDTO.class)
                                .flatMap(usuario -> Mono.just(ResponseEntity.status(409).body("El correo ya está registrado")));
                    } else {
                        // Si es 404, 400, 500, o text/plain, continuar con el registro
                        return metamapaApi.post()
                                .uri("/registrar")
                                .bodyValue(new UsuarioDTO(nombre, apellido, correo, "USER", contrasena))
                                .retrieve()
                                .onStatus(status -> status.value() == 400 || status.value() == 500,
                                        resp -> resp.bodyToMono(String.class).map(msg -> new RuntimeException(msg)))
                                .bodyToMono(UsuarioDTO.class)
                                .map(nuevo -> {
                                    try {
                                        String json = mapper.writeValueAsString(nuevo);
                                        return ResponseEntity.ok(json);
                                    } catch (Exception e) {
                                        return ResponseEntity.status(500).body("Error serializando usuario");
                                    }
                                });
                    }
                });
    }

    // DTO para desacoplar del core
    public static class UsuarioDTO {
        private String nombre;
        private String apellido;
        private String correo;
        private String rol;
        private String contrasena;

        public UsuarioDTO() {}
        public UsuarioDTO(String nombre, String apellido, String correo, String rol, String contrasena) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.rol = rol;
            this.contrasena = contrasena;
        }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getCorreo() { return correo; }
        public String getRol() { return rol; }
        public String getContrasena() { return contrasena; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public void setCorreo(String correo) { this.correo = correo; }
        public void setRol(String rol) { this.rol = rol; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }
}

