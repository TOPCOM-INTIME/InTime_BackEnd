package com.topcom.intime.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        			.exposedHeaders("Authorization")
        			.allowCredentials(true)
        			.allowedOrigins("*")
        			.allowedMethods(
        					HttpMethod.POST.name(), HttpMethod.GET.name(), 
                    		HttpMethod.PUT.name(), HttpMethod.DELETE.name(), 
                    		HttpMethod.OPTIONS.name()
                    );

    }
}