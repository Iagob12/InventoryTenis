package com.inventory.tenis.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.inventory.tenis.repositories.UserRepository;
import com.inventory.tenis.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
    @Autowired 
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        System.out.println("=== DEBUG SECURITY FILTER ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Token present: " + (token != null));
    
        if (token != null) {
            var email = tokenService.validateToken(token);
            System.out.println("Token validation result: " + email);
            
            // Verificar se o email não está vazio (token válido)
            if (email != null && !email.isEmpty()) {
                UserDetails user = userRepository.findByEmail(email);
                System.out.println("User found: " + (user != null));
        
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set for user: " + email);
                }
            }
        }
        
        System.out.println("Authentication in context: " + (SecurityContextHolder.getContext().getAuthentication() != null));
        System.out.println("==============================");
        
        filterChain.doFilter(request, response);
    }
    
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}