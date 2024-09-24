package com.is.IS_4k1_TFI_G2.seguridad;
<<<<<<<< HEAD:src/main/java/com/is/IS_4k1_TFI_G2/seguridad/SecurityConfig.java
========

>>>>>>>> devBrenda:src/main/java/com/is/IS_4k1_TFI_G2/seguridad/SeguridadConfiguracion.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SeguridadConfiguracion {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll()
                )
                .csrf((csrf) -> csrf.disable());

        return http.build();
    }
}
