package com.yato.urlShortenerb.config;

import com.yato.urlShortenerb.config.JWTUtils;
import com.yato.urlShortenerb.config.JwtAuthFilter;
import com.yato.urlShortenerb.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowCredentials(true);
                    corsConfig.setAllowedOrigins(
                            java.util.List.of("http://localhost:5174", "http://localhost:5173")
                    );
                    corsConfig.setAllowedMethods(
                            java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                    );
                    corsConfig.setAllowedHeaders(
                            java.util.List.of("Authorization", "Content-Type", "Accept")
                    );
                    corsConfig.setExposedHeaders(
                            java.util.List.of("Authorization")
                    );
                    return corsConfig;
                }))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/v3/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html",
                                "/s/**")
                        .permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
