package com.inventory.tenis.controllers;

import com.inventory.tenis.dto.MovimentacaoDTO;
import com.inventory.tenis.services.InventarioService;
import com.inventory.tenis.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private InventarioService inventoryService;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estatísticas do inventário
        stats.put("totalValue", inventoryService.getTotalInventoryValue());
        stats.put("productsInStock", inventoryService.getProductsInStockCount());
        
        // Movimentações recentes (últimas 10)
        List<MovimentacaoDTO> recentMovements = movimentacaoService.getAllMovimentacoes()
                .stream()
                .limit(10)
                .toList();
        stats.put("recentMovements", recentMovements);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getInventorySummary() {
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("inventory", inventoryService.getUserInventoryWithStock());
        summary.put("totalValue", inventoryService.getTotalInventoryValue());
        summary.put("totalProducts", inventoryService.getProductsInStockCount());
        
        return ResponseEntity.ok(summary);
    }
}