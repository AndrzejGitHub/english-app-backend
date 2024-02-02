package com.example.englishapp.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin((formLogin) -> formLogin
                                .loginProcessingUrl("/login")
                                .successHandler((request, response, authentication) -> {
                                    response.setStatus(HttpStatus.NO_CONTENT.value());
                                })
                                .failureHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(HttpStatus.BAD_REQUEST.value());
                                        }
                                )
                )
                .logout((logout) -> logout
                                .logoutSuccessUrl("/").permitAll()
                                .logoutSuccessHandler(
                                        (request, response, authentication) -> {
                                            response.setStatus(HttpStatus.NO_CONTENT.value());
                                        }
                                )
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/login","/logout","/api/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer -> {
                            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                                    (request, response, authException) -> response.setStatus(HttpStatus.UNAUTHORIZED.value())
                            );
                        }
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("DELETE", "GET", "POST", "PUT", "OPTION"));
        configuration.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
