package application.config;

import core.models.entities.usuario.Usuario;
import core.models.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String correo = authentication.getName();
        String contrasena = authentication.getCredentials().toString();
        Optional<Usuario> usuarioOpt = UsuarioRepository.getInstance().findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new BadCredentialsException("Usuario no encontrado");
        }
        if (!usuarioOpt.get().getContrasena().equals(contrasena)) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new UsernamePasswordAuthenticationToken(correo, contrasena, Collections.singletonList(authority));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
