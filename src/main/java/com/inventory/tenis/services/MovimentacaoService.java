package com.inventory.tenis.services;

import com.inventory.tenis.dto.MovimentacaoDTO;
import com.inventory.tenis.entities.Movimentacao;
import com.inventory.tenis.entities.StatusMovimentacao;
import com.inventory.tenis.entities.User;
import com.inventory.tenis.repositories.MovimentacaoRepository;
import com.inventory.tenis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return (User) userRepository.findByEmail(email);
    }

    public List<MovimentacaoDTO> getAllMovimentacoes() {
        User user = getCurrentUser();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserOrderByDataMovimentacaoDesc(user);
        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByTipo(StatusMovimentacao status) {
        User user = getCurrentUser();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserAndTipoOrderByDataMovimentacaoDesc(user, status);
        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        User user = getCurrentUser();
        
        // CORREÇÃO: Converter LocalDateTime para Date
        Date dataInicio = Date.from(inicio.atZone(ZoneId.systemDefault()).toInstant());
        Date dataFim = Date.from(fim.atZone(ZoneId.systemDefault()).toInstant());
        
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(user, dataInicio, dataFim);
        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }

    public List<MovimentacaoDTO> getMovimentacoesByInventario(Long inventarioId) {
        // CORREÇÃO: Converter Long para UUID se necessário
        UUID inventarioUuid;
        try {
            inventarioUuid = UUID.fromString(inventarioId.toString());
        } catch (Exception e) {
            // Se não conseguir converter, tenta buscar por ID numérico
            // Neste caso, você precisaria ajustar a query no repository
            throw new RuntimeException("ID do inventário deve ser um UUID válido");
        }
        
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByInventarioId(inventarioUuid);
        return movimentacoes.stream()
                .map(MovimentacaoDTO::new)
                .collect(Collectors.toList());
    }
}