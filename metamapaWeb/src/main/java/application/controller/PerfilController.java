package application.controller;

import application.service.AdminService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PerfilController {
    private final AdminService adminService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public PerfilController(AdminService adminService, OAuth2AuthorizedClientService authorizedClientService) {
        this.adminService = adminService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication, HttpServletRequest request) {
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            model.addAttribute("nombre", oAuth2User.getAttribute("name"));
            model.addAttribute("email", oAuth2User.getAttribute("email"));
            model.addAttribute("foto", oAuth2User.getAttribute("picture"));
            model.addAttribute("id", oAuth2User.getName());
            model.addAttribute("proveedor", oAuth2User.getAttribute("iss") != null ? oAuth2User.getAttribute("iss") : "Google");
            model.addAttribute("esAdmin", adminService.isAdmin(authentication));

            // Obtener el access token
            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
                if (client != null) {
                    String accessToken = client.getAccessToken().getTokenValue();
                    // Llamar a la API de Google People
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(accessToken);
                    headers.set("Accept", "application/json");
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<Map> response = restTemplate.exchange(
                            "https://people.googleapis.com/v1/people/me?personFields=names,birthdays", HttpMethod.GET, entity, Map.class);
                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        Map body = response.getBody();
                        // Nombre y apellido
                        if (body.containsKey("names")) {
                            var names = (java.util.List<Map>) body.get("names");
                            if (!names.isEmpty()) {
                                Map nameObj = names.get(0);
                                model.addAttribute("nombre", nameObj.getOrDefault("givenName", ""));
                                model.addAttribute("apellido", nameObj.getOrDefault("familyName", ""));
                            }
                        }
                        // Fecha de nacimiento y edad
                        if (body.containsKey("birthdays")) {
                            var birthdays = (java.util.List<Map>) body.get("birthdays");
                            if (!birthdays.isEmpty()) {
                                Map birthdayObj = birthdays.get(0);
                                Map dateObj = (Map) birthdayObj.get("date");
                                if (dateObj != null && dateObj.get("year") != null && dateObj.get("month") != null && dateObj.get("day") != null) {
                                    int year = (int) dateObj.get("year");
                                    int month = (int) dateObj.get("month");
                                    int day = (int) dateObj.get("day");
                                    LocalDate birthDate = LocalDate.of(year, month, day);
                                    int edad = Period.between(birthDate, LocalDate.now()).getYears();
                                    model.addAttribute("edad", edad);
                                }
                            }
                        }
                    }
                }
            }
        }
        String requestedWith = request.getHeader("X-Requested-With");
        if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {
            return "perfil/perfil :: sidebarPerfil";
        }
        return "redirect:/";
    }
}
