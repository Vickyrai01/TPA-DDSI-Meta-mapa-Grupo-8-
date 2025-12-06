package application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").authenticated()
//                        .requestMatchers("/css/**", "/js/**", "/img/**", "/", "/verColeccion/**", "/navegarColecciones/**", "/reportarSuceso/**").permitAll()
//                        .anyRequest().permitAll()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        // usa la URL por defecto de Spring para iniciar login con Google
//                        .loginPage("/oauth2/authorization/google")
//                        .defaultSuccessUrl("/admin", true)
//                )
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/static/**", "/webjars/**");
//    }
//}


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthProvider customAuthProvider;

    public SecurityConfig(CustomAuthProvider customAuthProvider) {
        this.customAuthProvider = customAuthProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF para /api/auth/** y /admin/**
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/api/auth/**"),
                                new AntPathRequestMatcher("/admin/**")
                        )
                )
                .authenticationProvider(customAuthProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/login", "/api/auth/login").permitAll()
                        .requestMatchers("/admin/**", "/panelDeControl/**").authenticated()
                        .requestMatchers("/perfil").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/api/auth/login")
                        .loginProcessingUrl("/api/auth/login")
                        .defaultSuccessUrl("/?login=success", true)
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}