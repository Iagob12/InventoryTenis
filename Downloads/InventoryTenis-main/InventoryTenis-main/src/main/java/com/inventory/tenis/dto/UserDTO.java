package com.inventory.tenis.dto;

import com.inventory.tenis.entities.StatusRole;
import com.inventory.tenis.entities.User;
import java.util.Date;
import java.util.UUID;

/**
 * DTO para representar dados do usuário sem problemas de lazy loading
 */
public class UserDTO {
    
    private UUID id;
    private String email;
    private String nome;
    private StatusRole role;
    private Date createdAt;
    
    public UserDTO() {}
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nome = user.getNome();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public StatusRole getRole() { return role; }
    public void setRole(StatusRole role) { this.role = role; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}