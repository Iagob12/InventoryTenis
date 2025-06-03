package com.inventory.tenis.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.services.TenisService;

@RestController
@RequestMapping("/api/tenis")
public class TenisController {

    private final TenisService service;

    public TenisController(TenisService service) {
        this.service = service;
    }

    @GetMapping
    public List<Tenis> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Tenis buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Tenis criar(@RequestBody Tenis tenis) {
        return service.salvar(tenis);
    }

    @PutMapping("/{id}")
    public Tenis atualizar(@PathVariable Long id, @RequestBody Tenis tenis) {
        tenis.setId(id);
        return service.salvar(tenis);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tênis excluído com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<Map<String, String>> deletarComDependencias(@PathVariable Long id) {
        try {
            service.deletarComDependencias(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tênis e todos os registros relacionados foram excluídos com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}/dependencies")
    public ResponseEntity<Map<String, Boolean>> verificarDependencias(@PathVariable Long id) {
        try {
            boolean temDependencias = service.temDependencias(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("hasDependencies", temDependencias);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Boolean> error = new HashMap<>();
            error.put("error", true);
            return ResponseEntity.badRequest().body(error);
        }
    }
}