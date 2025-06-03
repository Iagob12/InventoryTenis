package com.inventory.tenis.entities;

public enum StatusMovimentacao {
    ENTRADA("entrada"),
    SAIDA("saida"),
    AJUSTE("ajuste"),
    TRANSFERENCIA("transferencia");

    private String tipo;

    StatusMovimentacao(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}