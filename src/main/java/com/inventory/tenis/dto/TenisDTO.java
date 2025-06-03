package com.inventory.tenis.dto;

import com.inventory.tenis.entities.Tenis;

public class TenisDTO{

    private Long id;
    private String nome;
    private String codigo;
    private String descricao;
    private Float preco;
    private Double tamanho;
    private String urlImagem;

    public TenisDTO() {
    }

    public TenisDTO(Long id, String nome, String codigo, String descricao, Float preco, Double tamanho, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.preco = preco;
        this.tamanho = tamanho;
        this.urlImagem = urlImagem;
    }

    public TenisDTO(Tenis tenis) {
        this.id = tenis.getId();
        this.nome = tenis.getNome();
        this.codigo = tenis.getCodigo();
        this.descricao = tenis.getDescricao();
        this.preco = tenis.getPreco();
        this.tamanho = tenis.getTamanho();
        this.urlImagem = tenis.getUrlImagem();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Double getTamanho() {
        return tamanho;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    

}
