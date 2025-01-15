package br.com.roboticsmind.products.exceptions;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
@EnableWebMvc  // Optionally setup Spring MVC defaults (if you aren't using
// Spring Boot & haven't specified @EnableWebMvc elsewhere)
public class MvcConfiguration implements WebMvcConfigurer {
    @Bean(name = "simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r =
                new SimpleMappingExceptionResolver();

        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");
        mappings.setProperty("InvalidCreditCardException", "creditCardError");
        mappings.setProperty("ProductNotFoundException", "productNotFoundError");
        //mappings.setProperty("NoSuchElementException", "noSuchElementError");

        r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("error");    // No default
        r.setExceptionAttribute("ex");     // Default is "exception"
        r.setWarnLogCategory("example.MvcLogger");     // No default
        return r;
    }



}