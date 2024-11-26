package com.software.API.configuracion;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configuracion {

    /**
     * Define un bean para RestTemplate, que se usará para consumir APIs externas.
     *
     * @return RestTemplate configurado.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
