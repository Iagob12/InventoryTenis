package com.inventory.tenis.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.inventory.tenis.dto.InventarioCreateDTO;
import com.inventory.tenis.dto.InventarioResponseDTO;
import com.inventory.tenis.entities.Inventario;
import com.inventory.tenis.entities.Movimentacao;
import com.inventory.tenis.entities.StatusMovimentacao;
import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.entities.User;
import com.inventory.tenis.repositories.InventarioRepository;
import com.inventory.tenis.repositories.MovimentacaoRepository;
import com.inventory.tenis.repositories.TenisRepository;
import com.inventory.tenis.repositories.UserRepository;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private TenisRepository tenisRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return (User) userRepository.findByEmail(email);
    }

    public List<InventarioResponseDTO> getUserInventory() {
        User currentUser = getCurrentUser();
        List<Inventario> inventarios = inventarioRepository.findByUser(currentUser);
        
        return inventarios.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InventarioResponseDTO> getUserInventoryWithStock() {
        User currentUser = getCurrentUser();
        List<Inventario> inventarios = inventarioRepository.findByUserAndQuantidadeGreaterThan(currentUser, 0);
        
        return inventarios.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public InventarioResponseDTO addProductToInventory(InventarioCreateDTO dto) {
        User currentUser = getCurrentUser();
        
        Tenis tenis = tenisRepository.findById(dto.getTenisId())
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));
    
        Optional<Inventario> existingInventario = inventarioRepository.findByUserAndTenis(currentUser, tenis);
        
        Inventario inventario;
        if (existingInventario.isPresent()) {
            inventario = existingInventario.get();
            inventario.setQuantidade(inventario.getQuantidade() + dto.getQuantidade());
            inventario.setUpdatedAt(new Date());
        } else {
            inventario = new Inventario(currentUser, tenis, dto.getQuantidade());
        }
        
        inventario = inventarioRepository.save(inventario);
        
        // Registrar movimentação
        Movimentacao movimentacao = new Movimentacao(
                currentUser, 
                tenis, 
                StatusMovimentacao.ENTRADA, 
                dto.getQuantidade(), 
                dto.getDescricao()
        );
        movimentacaoRepository.save(movimentacao);
        
        return convertToResponseDto(inventario);
    }

    public void removeProductFromInventory(Long tenisId, Integer quantity, String description) {
        User currentUser = getCurrentUser();
        
        Tenis tenis = tenisRepository.findById(tenisId)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));

        Inventario inventario = inventarioRepository.findByUserAndTenis(currentUser, tenis)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no inventário"));

        if (inventario.getQuantidade() < quantity) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }

        inventario.setQuantidade(inventario.getQuantidade() - quantity);
        inventario.setUpdatedAt(new Date());
        inventarioRepository.save(inventario);

        Movimentacao movimentacao = new Movimentacao(
                currentUser, 
                tenis, 
                StatusMovimentacao.SAIDA, 
                quantity, 
                description
        );

        movimentacaoRepository.save(movimentacao);
    }

    public void adjustInventory(Long tenisId, Integer newQuantity, String description) {
        User currentUser = getCurrentUser();
        
        Tenis tenis = tenisRepository.findById(tenisId)
                .orElseThrow(() -> new RuntimeException("Tênis não encontrado"));

        Inventario inventario = inventarioRepository.findByUserAndTenis(currentUser, tenis)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no inventário"));

        Integer oldQuantity = inventario.getQuantidade();
        inventario.setQuantidade(newQuantity);
        inventario.setUpdatedAt(new Date());
        inventarioRepository.save(inventario);

        Movimentacao movimentacao = new Movimentacao(
                currentUser, 
                tenis, 
                StatusMovimentacao.AJUSTE, 
                newQuantity - oldQuantity, 
                description != null ? description : "Ajuste de estoque"
        );
        
        movimentacaoRepository.save(movimentacao);
    }

    public Double getTotalInventoryValue() {
        User currentUser = getCurrentUser();
        List<Inventario> inventarios = inventarioRepository.findByUser(currentUser);
        
        return inventarios.stream()
                .mapToDouble(inv -> inv.getQuantidade() * inv.getTenis().getPreco())
                .sum();
    }

    public Long getProductsInStockCount() {
        User currentUser = getCurrentUser();
        return inventarioRepository.countByUserAndQuantidadeGreaterThan(currentUser, 0);
    }

    private InventarioResponseDTO convertToResponseDto(Inventario inventario) {
        Tenis tenis = inventario.getTenis();
        Double valorTotal = (double) (inventario.getQuantidade() * tenis.getPreco());
        
        return new InventarioResponseDTO(
                inventario.getId(),
                tenis.getId(),
                tenis.getNome(),
                tenis.getCodigo(),
                tenis.getDescricao(),
                tenis.getTamanho(),
                tenis.getUrlImagem(),
                inventario.getQuantidade(),
                tenis.getPreco(), // Usa sempre o preço atual do tênis
                valorTotal,
                inventario.getCreatedAt(),
                inventario.getUpdatedAt()
        );
    }
}