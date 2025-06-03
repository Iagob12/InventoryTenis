package com.inventory.tenis.controllers;

import com.inventory.tenis.dto.MovimentacaoDTO;
import com.inventory.tenis.entities.StatusMovimentacao;
import com.inventory.tenis.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoDTO>> getAllMovimentacoes() {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getAllMovimentacoes();
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByTipo(@PathVariable StatusMovimentacao tipo) {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByTipo(tipo);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByPeriodo(inicio, fim);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByInventario(@PathVariable String inventarioId) {
        try {
            // Converter String para Long se necessário, ou manter como String para UUID
            Long id = Long.parseLong(inventarioId);
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByInventario(id);
            return ResponseEntity.ok(movimentacoes);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}