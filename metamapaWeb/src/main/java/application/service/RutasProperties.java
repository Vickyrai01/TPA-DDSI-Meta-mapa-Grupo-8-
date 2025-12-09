package application.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "metamapa.api")
@Getter
@Setter
public class RutasProperties {
    private String baseUrl;
    private String adminBaseUrl;
    private String estadisticasBaseUrl;

    public RutasProperties() {
        System.out.println(">>> RutasProperties creado!");
    }

    @PostConstruct
    public void init() {
        System.out.println("BASE URL = " + baseUrl);
        System.out.println("ADMIN URL = " + adminBaseUrl);
        System.out.println("ESTADISTICAS URL = " + estadisticasBaseUrl);
    }

}
