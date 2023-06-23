package org.zamzam.orderservice.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.zamzam.orderservice.service.StringToDateConverter;

@Configuration
public class AppConfig {
    @Autowired
    private StringToDateConverter stringToDateConverter;
    @Bean
    public WebFluxConfigurer configurer(){
        return new WebFluxConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(stringToDateConverter);
            }
        };
    }
}
