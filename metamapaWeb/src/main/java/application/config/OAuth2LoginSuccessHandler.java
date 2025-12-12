package application.config;

import application.service.RutasProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String metamapaApi;

    public OAuth2LoginSuccessHandler(RutasProperties props) {
        this.metamapaApi = props.getBaseUrl() + "/usuarios";
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        try {
            if (authentication.getPrincipal() instanceof DefaultOAuth2User oAuth2User) {

                String email   = (String) oAuth2User.getAttributes().get("email");
                String name    = (String) oAuth2User.getAttributes().get("name");
                String picture = (String) oAuth2User.getAttributes().get("picture");

                System.out.println("[SSO] Login exitoso con Google. Email: " + email);

                if (email != null) {

                    // ============ 1) BUSCAR USUARIO EN EL CORE ============
                    UsuarioDTO existing = null;
                    try {
                        String url = metamapaApi + "/buscar?correo={correo}";
                        ResponseEntity<UsuarioDTO> resp =
                                restTemplate.getForEntity(url, UsuarioDTO.class, email);

                        if (resp.getStatusCode().is2xxSuccessful()) {
                            existing = resp.getBody();
                        }
                    } catch (HttpClientErrorException.NotFound ex) {
                        // 404 -> usuario no existe en el CORE (esto es lo esperado a veces)
                        System.out.println("[SSO] Usuario no encontrado en CORE, se creará: " + email);
                    } catch (RestClientResponseException ex) {
                        System.out.println("[SSO] Error consultando CORE: " + ex.getStatusText());
                    } catch (Exception ex) {
                        System.out.println("[SSO] Error inesperado consultando CORE: " + ex.getMessage());
                    }

                    // ============ 2) SI NO EXISTE, LO CREAMOS EN EL CORE ============
                    if (existing == null) {
                        System.out.println("[SSO] Creando usuario en CORE para: " + email);

                        UsuarioDTO nuevo = new UsuarioDTO(
                                name,
                                email,
                                "USER",
                                null  // contraseña null: tu CORE puede ignorarla si viene de SSO
                        );

                        try {
                            String url = metamapaApi + "/registrar";
                            ResponseEntity<UsuarioDTO> resp =
                                    restTemplate.postForEntity(url, nuevo, UsuarioDTO.class);

                            if (resp.getStatusCode() == HttpStatus.CREATED
                                    || resp.getStatusCode().is2xxSuccessful()) {
                                System.out.println("[SSO] Usuario creado en CORE: " + email);
                            } else {
                                System.out.println("[SSO] El CORE devolvió estado no exitoso al registrar: "
                                        + resp.getStatusCode());
                            }
                        } catch (RestClientResponseException ex) {
                            System.out.println("[SSO] Error HTTP al crear usuario en CORE: " + ex.getStatusText());
                        } catch (Exception ex) {
                            System.out.println("[SSO] Error inesperado al crear usuario en CORE: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("[SSO] Usuario ya existía en el CORE: " + email);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[SSO] Error en successHandler SSO (no corta el login): " + e.getMessage());
        }

        response.sendRedirect("/mapa");
    }

    // DTO interno solo para hablar con el CORE
    public static class UsuarioDTO {
        private String nombre;
        private String correo;
        private String rol;
        private String contrasena;
        private LocalDate localDate;

        public UsuarioDTO() {}

        public UsuarioDTO(String nombre, String correo, String rol, String contrasena) {
            this.nombre = nombre;
            this.correo = correo;
            this.rol = rol;
            this.contrasena = contrasena;
            localDate = null;
        }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }
}
