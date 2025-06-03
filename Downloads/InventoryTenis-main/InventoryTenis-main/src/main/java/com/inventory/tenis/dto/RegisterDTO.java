package com.inventory.tenis.dto;

import com.inventory.tenis.entities.StatusRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String password;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Role é obrigatória")
    private StatusRole role;
    
    public RegisterDTO() {}
    
    public RegisterDTO(String email, String password, String nome, StatusRole role) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.role = role;
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public StatusRole getRole() { return role; }
    public void setRole(StatusRole role) { this.role = role; }
}