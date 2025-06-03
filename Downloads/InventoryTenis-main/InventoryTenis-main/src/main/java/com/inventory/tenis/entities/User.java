package com.inventory.tenis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nome;
    
    @Enumerated(EnumType.STRING)
    private StatusRole role;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    // SOLUÇÃO 1: Ignorar na serialização JSON
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inventario> inventarios;
    
    // SOLUÇÃO 1: Ignorar na serialização JSON
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tenis> tenis;

    public User() {}

    // Construtor original (manter para compatibilidade, se necessário)
    public User(String email, String password, StatusRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = new Date();
    }

    // Novo construtor que inclui o nome
    public User(String email, String password, String nome, StatusRole role) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.role = role;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public StatusRole getRole() { return role; }
    public void setRole(StatusRole role) { this.role = role; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public List<Inventario> getInventarios() { return inventarios; }
    public void setInventarios(List<Inventario> inventarios) { this.inventarios = inventarios; }
    
    public List<Tenis> getTenis() { return tenis; }
    public void setTenis(List<Tenis> tenis) { this.tenis = tenis; }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == StatusRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}