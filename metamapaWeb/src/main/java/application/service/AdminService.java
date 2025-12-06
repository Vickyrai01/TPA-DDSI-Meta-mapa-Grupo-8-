package application.service;

import application.config.AdminEmailsConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminEmailsConfig adminEmailsConfig;

    public AdminService(AdminEmailsConfig adminEmailsConfig) {
        this.adminEmailsConfig = adminEmailsConfig;
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User oAuth2User) {
            String email = (String) oAuth2User.getAttributes().get("email");
            return adminEmailsConfig.getAdminEmails().contains(email);
        }
        return false;
    }
}
