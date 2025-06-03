package com.inventory.tenis.dto;

import com.inventory.tenis.entities.Movimentacao;
import com.inventory.tenis.entities.StatusMovimentacao;

import java.util.Date;
import java.util.UUID;

public class MovimentacaoDTO {
    
    private UUID id;
    private TenisDTO tenis;
    private StatusMovimentacao tipo;
    private Integer quantidade;
    private Date dataMovimentacao;
    private String descricao;

    public MovimentacaoDTO() {}

    public MovimentacaoDTO(Movimentacao movimentacao) {
        this.id = movimentacao.getId();
        this.tenis = new TenisDTO(movimentacao.getTenis());
        this.tipo = movimentacao.getTipo();
        this.quantidade = movimentacao.getQuantidade();
        this.dataMovimentacao = movimentacao.getDataMovimentacao();
        this.descricao = movimentacao.getDescricao();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public TenisDTO getTenis() { return tenis; }
    public void setTenis(TenisDTO tenis) { this.tenis = tenis; }
    
    public StatusMovimentacao getTipo() { return tipo; }
    public void setTipo(StatusMovimentacao tipo) { this.tipo = tipo; }
    
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    
    public Date getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(Date dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}