package com.example.cinefile.seguranca;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfiguracaoSeguranca {

    @Bean
    public SecurityFilterChain configurarFiltroSeguranca(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 ENDPOINTS PÚBLICOS
                        .requestMatchers("/api/autenticacao/**").permitAll() // ✅ Login customizado
                        .requestMatchers(HttpMethod.GET, "/api/obras/**").permitAll()
                        .requestMatchers("/tmdb/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔐 ENDPOINTS AUTENTICADOS
                        .requestMatchers(HttpMethod.POST, "/api/avaliacoes").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/**/comentarios").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/watchlist/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/assistido/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/logs-visualizacao").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/minhas-configuracoes").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/minhas-configuracoes").authenticated()

                        // 👑 ENDPOINTS ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/obras").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/obras/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/obras/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/categorias").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/obras/**/temporadas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/obras/**/temporadas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/tmdb/categorias/sincronizar/**").hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                // ✅ REMOVA O BASIC AUTH para evitar conflito
                // .httpBasic(Customizer.withDefaults()) // 🔥 COMENTE ESTA LINHA
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ USE allowedOrigins EM VEZ DE allowedOriginPatterns
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:8080"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "X-Requested-With", "Accept",
                "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers",
                "X-CSRF-TOKEN"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "Content-Type"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder codificadorSenha() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gerenciadorAutenticacao(AuthenticationConfiguration configuracao) throws Exception {
        return configuracao.getAuthenticationManager();
    }
}