package com.escola_musica.nota_certa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return resolver -> resolver.setMaxPageSize(100);
    }
}