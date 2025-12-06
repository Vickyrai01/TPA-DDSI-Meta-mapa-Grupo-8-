package application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class AdminEmailsConfig {
    @Value("${metamapa.admin-emails}")
    private String adminEmails;

    public List<String> getAdminEmails() {
        return List.of(adminEmails.split(","));
    }
}
