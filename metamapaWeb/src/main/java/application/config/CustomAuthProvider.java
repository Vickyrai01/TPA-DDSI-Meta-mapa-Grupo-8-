package application.config;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

    @Component
    public class CustomAuthProvider implements AuthenticationProvider {

        private final RestTemplate restTemplate = new RestTemplate();
        private final String coreBaseUrl = "http://localhost:8081/core/api/usuarios";

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String rawCorreo = authentication.getName();
            String contrasena = authentication.getCredentials().toString();

            if (rawCorreo == null || rawCorreo.trim().isEmpty()) {
                throw new BadCredentialsException("Correo inválido");
            }

            String correo = rawCorreo.trim().toLowerCase();
            System.out.println("[AUTH] Intentando autenticar: '" + correo + "'");

            UsuarioDTO usuario = null;

            // ============ 1) BUSCAR USUARIO EN EL CORE ============
            try {
                String url = coreBaseUrl + "/buscar?correo={correo}";
                ResponseEntity<UsuarioDTO> resp =
                        restTemplate.getForEntity(url, UsuarioDTO.class, correo);

                if (resp.getStatusCode().is2xxSuccessful()) {
                    usuario = resp.getBody();
                }
            } catch (HttpClientErrorException.NotFound ex) {
                // 404 -> usuario no existe en el CORE
                System.out.println("[AUTH] Usuario no encontrado en CORE: " + correo);
                throw new BadCredentialsException("Usuario no encontrado");
            } catch (RestClientResponseException ex) {
                System.out.println("[AUTH] Error HTTP consultando CORE: " + ex.getStatusText());
                throw new AuthenticationServiceException("Error al comunicarse con el CORE", ex);
            } catch (Exception ex) {
                System.out.println("[AUTH] Error inesperado consultando CORE: " + ex.getMessage());
                throw new AuthenticationServiceException("Error al autenticarse contra el CORE", ex);
            }

            if (usuario == null) {
                throw new BadCredentialsException("Usuario no encontrado");
            }

            // ============ 2) VALIDAR CONTRASEÑA ============
            if (usuario.getContrasena() == null || !usuario.getContrasena().equals(contrasena)) {
                throw new BadCredentialsException("Contraseña incorrecta");
            }

            // ============ 3) ARMAR AUTHORITIES Y ATRIBUTOS ============
            String rol = (usuario.getRol() != null) ? usuario.getRol() : "USER";
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());

            Map<String, Object> attributes = new HashMap<>();
            String nombreCompleto =
                    (usuario.getNombre() != null ? usuario.getNombre() : "") + " " +
                            (usuario.getApellido() != null ? usuario.getApellido() : "");
            attributes.put("name", nombreCompleto.trim());
            attributes.put("email", usuario.getCorreo());
            attributes.put("picture", usuario.getFoto());

            System.out.println("[AUTH] Autenticación exitosa para: " + usuario.getCorreo());

            return new UsernamePasswordAuthenticationToken(
                    attributes,
                    contrasena,
                    Collections.singletonList(authority)
            );
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(UsernamePasswordAuthenticationToken.class);
        }

        // DTO interno para mapear lo que devuelve el CORE
        public static class UsuarioDTO {
            private Integer id;
            private String nombre;
            private String apellido;
            private String correo;
            private String rol;
            private String foto;
            private String contrasena;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public String getApellido() {
                return apellido;
            }

            public void setApellido(String apellido) {
                this.apellido = apellido;
            }

            public String getCorreo() {
                return correo;
            }

            public void setCorreo(String correo) {
                this.correo = correo;
            }

            public String getRol() {
                return rol;
            }

            public void setRol(String rol) {
                this.rol = rol;
            }

            public String getFoto() {
                return foto;
            }

            public void setFoto(String foto) {
                this.foto = foto;
            }

            public String getContrasena() {
                return contrasena;
            }

            public void setContrasena(String contrasena) {
                this.contrasena = contrasena;
            }
        }

    }

