package com.inventory.tenis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.tenis.dto.AuthenticationDTO;
import com.inventory.tenis.dto.RegisterDTO;
import com.inventory.tenis.services.LoginService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class LoginController {
   
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        return loginService.login(authenticationDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO registerDTO){
        return loginService.register(registerDTO);
    }
}