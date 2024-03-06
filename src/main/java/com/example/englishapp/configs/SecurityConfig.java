package com.example.englishapp.configs;

import com.example.englishapp.controllers.CustomResponseEntityExceptionHandler;
import com.example.englishapp.models.UserRole;
import com.example.englishapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityConfig {

    final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler.class);

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userService.findByEmail(email)
                .map((user) -> User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities(convertAuthorities(user.getRoles()))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found"));
    }

    private Collection<? extends GrantedAuthority> convertAuthorities(Set<UserRole> userRoles) {
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.name()))
                .toList();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                                    logger.error("Login exception", exception);
                                    logger.error("URL request: {}", request.getRequestURI());
                                    logger.error("Exception: {}", exception.getMessage());
                                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                                }
                        )
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/logout").permitAll()
                        .logoutSuccessHandler(
                                (request, response, authentication) -> {
                                    response.setStatus(HttpStatus.NO_CONTENT.value());
                                }
                        )
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer -> {
                            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                                    (request, response, authException) -> {
                                        logger.error("Login exception", authException);
                                        logger.error("URL request: {}", request.getRequestURI());
                                        logger.error("Exception: {}", authException.getMessage());
                                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                    }
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
