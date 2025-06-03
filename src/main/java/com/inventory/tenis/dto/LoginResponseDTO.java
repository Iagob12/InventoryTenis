package com.inventory.tenis.dto;

public class LoginResponseDTO {
    
    private String token;
    private UserDTO user; // Adicionar dados do usuário
    
    public LoginResponseDTO() {}
    
    // Construtor original para compatibilidade
    public LoginResponseDTO(String token) {
        this.token = token;
    }
    
    // Novo construtor com dados do usuário
    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
}