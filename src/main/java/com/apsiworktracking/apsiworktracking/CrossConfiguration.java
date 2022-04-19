package com.apsiworktracking.apsiworktracking;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class CrossConfiguration implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Access-Control-Allow-Origin", "*")
                .allowedHeaders("Access-Control-Allow-Credentials", "true")
                .allowedHeaders("Access-Control-Allow-Headers", "content-type, if-none-match")
                .allowedHeaders("Access-Control-Allow-Methods", "POST,GET,OPTIONS")
                .allowedHeaders("Access-Control-Max-Age", "3600")
                .allowCredentials(false).maxAge(3600);
    }
}