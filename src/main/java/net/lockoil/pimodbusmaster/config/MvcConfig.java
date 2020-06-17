package net.lockoil.pimodbusmaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
    public void addResourceHandlers(
      ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/** **")
          .addResourceLocations("/WEB-INF/classes/static/build/static/");
        registry.addResourceHandler("/**.js")
          .addResourceLocations("/WEB-INF/classes/static/build/");
        registry.addResourceHandler("/**.json")
          .addResourceLocations("/WEB-INF/classes/static/build/");
        registry.addResourceHandler("/**.ico")
          .addResourceLocations("/WEB-INF/classes/static/build/");
        registry.addResourceHandler("/index.html")
          .addResourceLocations("/WEB-INF/classes/static/build/index.html");
    }
}