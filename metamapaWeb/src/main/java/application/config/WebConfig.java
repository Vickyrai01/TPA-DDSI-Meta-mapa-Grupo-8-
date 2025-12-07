package application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.hechos-dir:uploads/hechos}")
    private String hechosDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path hechosPath = Paths.get(hechosDir).toAbsolutePath().normalize();
        String location = "file:" + hechosPath.toString() + "/";

        registry.addResourceHandler("/media/hechos/**")
                .addResourceLocations(location);
    }

}
