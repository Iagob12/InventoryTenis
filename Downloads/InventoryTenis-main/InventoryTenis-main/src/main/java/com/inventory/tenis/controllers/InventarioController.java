package com.inventory.tenis.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.tenis.dto.InventarioCreateDTO;
import com.inventory.tenis.dto.InventarioResponseDTO;
import com.inventory.tenis.services.InventarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
public class InventarioController {
    
    @Autowired
    private InventarioService inventarioService;
    
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventory() {
        List<InventarioResponseDTO> inventory = inventarioService.getUserInventory();
        return ResponseEntity.ok(inventory);
    }
    
    @GetMapping("/stock")
    public ResponseEntity<List<InventarioResponseDTO>> getUserInventoryWithStock() {
        List<InventarioResponseDTO> inventory = inventarioService.getUserInventoryWithStock();
        return ResponseEntity.ok(inventory);
    }
    
    @PostMapping("/add")
    public ResponseEntity<InventarioResponseDTO> addProductToInventory(
            @Valid @RequestBody InventarioCreateDTO dto) {
        try {
            InventarioResponseDTO response = inventarioService.addProductToInventory(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/remove/{tenisId}")
    public ResponseEntity<Void> removeProductFromInventory(
            @PathVariable Long tenisId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String description) {
        try {
            inventarioService.removeProductFromInventory(tenisId, quantity, description);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/adjust/{tenisId}")
    public ResponseEntity<Void> adjustInventory(
            @PathVariable Long tenisId,
            @RequestParam Integer newQuantity,
            @RequestParam(required = false) String description) {
        try {
            inventarioService.adjustInventory(tenisId, newQuantity, description);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/value")
    public ResponseEntity<Double> getTotalInventoryValue() {
        Double totalValue = inventarioService.getTotalInventoryValue();
        return ResponseEntity.ok(totalValue);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> getProductsInStockCount() {
        Long count = inventarioService.getProductsInStockCount();
        return ResponseEntity.ok(count);
    }
}