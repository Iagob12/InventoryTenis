package com.inventory.tenis.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InventarioCreateDTO {
    
    @NotNull(message = "ID do tênis é obrigatório")
    private Long tenisId;
    
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;
    
    private String descricao;

    public InventarioCreateDTO() {}

    public InventarioCreateDTO(Long tenisId, Integer quantidade, String descricao) {
        this.tenisId = tenisId;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public Long getTenisId() {
        return tenisId;
    }

    public void setTenisId(Long tenisId) {
        this.tenisId = tenisId;
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
}