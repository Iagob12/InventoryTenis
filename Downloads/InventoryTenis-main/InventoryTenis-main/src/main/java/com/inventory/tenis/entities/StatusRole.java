package com.inventory.tenis.entities;

public enum StatusRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    StatusRole (String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
