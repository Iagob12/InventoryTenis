package com.inventory.tenis.dto;

import java.util.UUID;
import java.util.Date;

public class InventarioResponseDTO {
    
    private UUID id;
    private Long tenisId;
    private String tenisNome;
    private String tenisCodigo;
    private String tenisDescricao;
    private Double tenisTamanho;
    private String tenisUrlImagem;
    private Integer quantidade;
    private Float precoUnitario;
    private Double valorTotal;
    private Date createdAt;
    private Date updatedAt;

    public InventarioResponseDTO() {}

    public InventarioResponseDTO (UUID id, Long tenisId, String tenisNome, String tenisCodigo, String tenisDescricao, Double tenisTamanho, String tenisUrlImagem, Integer quantidade, Float precoUnitario, Double valorTotal, Date createdAt, Date updatedAt) {
        this.id = id;
        this.tenisId = tenisId;
        this.tenisNome = tenisNome;
        this.tenisCodigo = tenisCodigo;
        this.tenisDescricao = tenisDescricao;
        this.tenisTamanho = tenisTamanho;
        this.tenisUrlImagem = tenisUrlImagem;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getTenisId() {
        return tenisId;
    }

    public void setTenisId(Long tenisId) {
        this.tenisId = tenisId;
    }

    public String getTenisNome() {
        return tenisNome;
    }

    public void setTenisNome(String tenisNome) {
        this.tenisNome = tenisNome;
    }

    public String getTenisCodigo() {
        return tenisCodigo;
    }

    public void setTenisCodigo(String tenisCodigo) {
        this.tenisCodigo = tenisCodigo;
    }

    public String getTenisDescricao() {
        return tenisDescricao;
    }

    public void setTenisDescricao(String tenisDescricao) {
        this.tenisDescricao = tenisDescricao;
    }

    public Double getTenisTamanho() {
        return tenisTamanho;
    }

    public void setTenisTamanho(Double tenisTamanho) {
        this.tenisTamanho = tenisTamanho;
    }

    public String getTenisUrlImagem() {
        return tenisUrlImagem;
    }

    public void setTenisUrlImagem(String tenisUrlImagem) {
        this.tenisUrlImagem = tenisUrlImagem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}