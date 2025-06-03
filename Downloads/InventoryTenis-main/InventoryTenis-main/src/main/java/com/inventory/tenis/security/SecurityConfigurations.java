package com.inventory.tenis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean 
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Autenticação
                .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                
                // Tênis - Endpoints públicos
                .requestMatchers(HttpMethod.GET, "/api/tenis").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tenis/**").permitAll()
                
                // Tênis - Endpoints protegidos
                .requestMatchers(HttpMethod.POST, "/api/tenis").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/tenis/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/tenis/**").authenticated()
                
                // CORREÇÃO: Inventário - Todos os endpoints protegidos
                .requestMatchers("/api/inventory/**").authenticated()
                
                // CORREÇÃO: Movimentações - Todos os endpoints protegidos
                .requestMatchers("/api/movimentacoes/**").authenticated()
                
                // CORREÇÃO: Dashboard - Todos os endpoints protegidos
                .requestMatchers("/api/dashboard/**").authenticated()
                
                // Outras requisições
                .anyRequest().permitAll()
                )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}