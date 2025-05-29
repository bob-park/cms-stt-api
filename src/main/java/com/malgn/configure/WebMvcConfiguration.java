package com.malgn.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3_600L)
            .exposedHeaders(
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CONTENT_DISPOSITION,
                HttpHeaders.CONTENT_LENGTH,
                HttpHeaders.ORIGIN,
                HttpHeaders.ACCEPT);
    }
}
