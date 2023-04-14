// package mg.recipe;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.*;

// @Configuration
// @EnableWebMvc
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/upload/**")
//                 .addResourceLocations("file:/C:/KenshuProject/upload/");

//         registry.addResourceHandler("/css/**")
//                 .addResourceLocations("file:/C:/KenshuProject/recipe/src/main/resources/static/css/");

//         registry.addResourceHandler("/js/**")
//                 .addResourceLocations("file:/C:/KenshuProject/recipe/src/main/resources/static/js/");

//         registry.addResourceHandler("/assets/img/**")
//                 .addResourceLocations("file:/C:/KenshuProject/recipe/src/main/resources/static/assets/img/");

//         registry.addResourceHandler("/assets/icon/**")
//                 .addResourceLocations("file:/C:/KenshuProject/recipe/src/main/resources/static/assets/icon/");
//     }

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOriginPatterns("*")
//                 .allowedMethods("GET", "POST", "PUT", "DELETE")
//                 .allowedHeaders("*")
//                 .allowCredentials(true);
//     }
// }
