package com.example.clearcard.config;

import com.example.clearcard.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService uds;

    @Bean BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    AuthenticationManager authManager(BCryptPasswordEncoder enc) {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(enc);
        provider.setUserDetailsService(uds);
        return new ProviderManager(provider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> {})
                .csrf(c -> c.disable()) // JWT-based; CSRF disabled
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers(
                            "/auth/**", "/api/auth/**",
                            "/swagger-ui/**", "/v3/api-docs/**",
                            "/jobs", "/jobs/**", "/api/jobs", "/api/jobs/**",
                            "/sql", "/api/sql", "/csv", "/api/csv",
                            "/articles", "/api/articles", "/articles/**", "/api/articles/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Important: non-blocking JWT filter BEFORE UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
            "http://localhost", "http://localhost:*",      // nginx (:80) and any port
            "http://127.0.0.1", "http://127.0.0.1:*"       // local variants
            // add your real domain(s) here in prod, e.g. "https://app.example.com"
        ));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        cfg.setAllowedHeaders(List.of(
                "Authorization","Content-Type","Accept","X-Requested-With",
                "X-Job-Title","X-Job-Config","X-XSRF-TOKEN","X-CSRF-TOKEN"
        ));
        cfg.setAllowCredentials(true);
        var src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
}
