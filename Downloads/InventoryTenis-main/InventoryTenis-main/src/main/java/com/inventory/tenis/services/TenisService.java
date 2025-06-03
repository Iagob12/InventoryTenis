package com.inventory.tenis.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.Optional;
import com.inventory.tenis.entities.Inventario;
import com.inventory.tenis.entities.Movimentacao;
import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.entities.User;
import com.inventory.tenis.repositories.InventarioRepository;
import com.inventory.tenis.repositories.MovimentacaoRepository;
import com.inventory.tenis.repositories.TenisRepository;
import com.inventory.tenis.repositories.UserRepository;

@Service
public class TenisService {

    private final TenisRepository repository;
    private final UserRepository userRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public TenisService(TenisRepository repository, UserRepository userRepository, 
                      InventarioRepository inventarioRepository, MovimentacaoRepository movimentacaoRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public Tenis salvar(Tenis tenis) {
        User currentUser = getCurrentUser();
        
        // Se for um novo tênis
        if (tenis.getId() == null) {
            tenis.setUser(currentUser);
            tenis.setCreatedAt(new Date());
        } else {
            // Verificar se o tênis pertence ao usuário atual
            Tenis existingTenis = buscarPorId(tenis.getId());
            if (!existingTenis.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Você não tem permissão para editar este tênis");
            }
            tenis.setUser(currentUser);
            tenis.setUpdatedAt(new Date());
        }
        
        return repository.save(tenis);
    }

    public List<Tenis> listar() {
        User currentUser = getCurrentUser();
        // Retorna apenas os tênis do usuário logado
        return repository.findByUser(currentUser);
    }

    public Tenis buscarPorId(Long id) {
        User currentUser = getCurrentUser();
        Tenis tenis = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));
        
        // Verificar se o tênis pertence ao usuário atual
        if (!tenis.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar este tênis");
        }
        
        return tenis;
    }

    @Transactional
    public void deletar(Long id) {
        User currentUser = getCurrentUser();
        Tenis tenis = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));
        
        // Verificar se o tênis pertence ao usuário atual
        if (!tenis.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este tênis");
        }
        
        // Verificar se existem registros relacionados
        List<Inventario> inventarios = inventarioRepository.findByUserAndTenis(currentUser, tenis)
        .map(Arrays::asList)
        .orElse(Collections.emptyList());
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserAndTenis(currentUser, tenis);
        
        if (!inventarios.isEmpty() || !movimentacoes.isEmpty()) {
            throw new RuntimeException("Não é possível excluir este tênis pois existem registros relacionados no inventário ou movimentações. " +
                    "Para excluir, primeiro remova todos os registros de inventário e movimentações relacionadas a este produto.");
        }
        
        repository.deleteById(id);
    }

    @Transactional
    public void deletarComDependencias(Long id) {
        User currentUser = getCurrentUser();
        Tenis tenis = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));
        
        // Verificar se o tênis pertence ao usuário atual
        if (!tenis.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este tênis");
        }
        
        // Primeiro, deletar todas as movimentações relacionadas
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserAndTenis(currentUser, tenis);
        if (!movimentacoes.isEmpty()) {
            movimentacaoRepository.deleteAll(movimentacoes);
        }
        
        // Depois, deletar todos os registros de inventário relacionados
        List<Inventario> inventarios = inventarioRepository.findByUserAndTenis(currentUser, tenis);
        if (!inventarios.isEmpty()) {
            inventarioRepository.deleteAll(inventarios);
        }
        
        // Por fim, deletar o tênis
        repository.deleteById(id);
    }

    public boolean temDependencias(Long id) {
        User currentUser = getCurrentUser();
        Tenis tenis = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));
        
        // Verificar se o tênis pertence ao usuário atual
        if (!tenis.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar este tênis");
        }
        
        List<Inventario> inventarios = inventarioRepository.findByUserAndTenis(currentUser, tenis)
        .map(Arrays::asList)
        .orElse(Collections.emptyList());
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByUserAndTenis(currentUser, tenis);
        
        return !inventarios.isEmpty() || !movimentacoes.isEmpty();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = authentication.getName();
        
        if (email == null || email.equals("anonymousUser")) {
            throw new RuntimeException("Usuário não autenticado");
        }

        User user = (User) userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return user;
    }
}