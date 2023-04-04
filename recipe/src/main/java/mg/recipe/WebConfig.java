package mg.recipe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:/C:/test/upload/");

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("file:/C:/test/recipe/src/main/resources/static/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:/C:/test/recipe/src/main/resources/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:/C:/test/recipe/src/main/resources/static/css/");

        registry.addResourceHandler("/assets/img/**")
                .addResourceLocations("file:/C:/test/recipe/src/main/resources/static/assets/img/");

        registry.addResourceHandler("/assets/icon/**")
                .addResourceLocations("file:/C:/test/recipe/src/main/resources/static/assets/icon/");
    }
}
