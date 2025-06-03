package com.inventory.tenis.services;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.inventory.tenis.dto.AuthenticationDTO;
import com.inventory.tenis.dto.LoginResponseDTO;
import com.inventory.tenis.dto.RegisterDTO;
import com.inventory.tenis.dto.UserDTO;
import com.inventory.tenis.entities.User;
import com.inventory.tenis.repositories.UserRepository;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    private AuthenticationManager authenticationManager;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    } 

    public ResponseEntity<Object> login(AuthenticationDTO data) {
        try {
            authenticationManager = context.getBean(AuthenticationManager.class);

            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            
            User user = (User) auth.getPrincipal();
            var token = tokenService.generateToken(user);
            
            // SOLUÇÃO: Retornar dados do usuário usando DTO + token
            LoginResponseDTO response = new LoginResponseDTO(token, new UserDTO(user));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }

    public ResponseEntity<Object> register(RegisterDTO registerDTO) {
        if (this.userRepository.findByEmail(registerDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email já está em uso");
        }
        
        try {
            String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.getPassword());
            User newUser = new User(registerDTO.getEmail(), encryptedPassword, registerDTO.getNome(), registerDTO.getRole());
            newUser.setCreatedAt(new Date(System.currentTimeMillis()));
            User savedUser = this.userRepository.save(newUser);
            
            // SOLUÇÃO: Retornar DTO ao invés da entidade completa
            return ResponseEntity.ok().body(new UserDTO(savedUser));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }
}