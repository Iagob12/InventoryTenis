package com.inventory.tenis.entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tb_movimentacao")
public class Movimentacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "tenis_id", nullable = false)
    private Tenis tenis;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMovimentacao tipo;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(length = 500)
    private String descricao;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_movimentacao", nullable = false)
    private Date dataMovimentacao;
    
    public Movimentacao() {
    }
    
    public Movimentacao(User user, Tenis tenis, StatusMovimentacao tipo, Integer quantidade, String descricao) {
        this.user = user;
        this.tenis = tenis;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.dataMovimentacao = new Date();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Tenis getTenis() {
        return tenis;
    }
    
    public void setTenis(Tenis tenis) {
        this.tenis = tenis;
    }
    
    public StatusMovimentacao getTipo() {
        return tipo;
    }
    
    public void setTipo(StatusMovimentacao tipo) {
        this.tipo = tipo;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }
    
    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
}