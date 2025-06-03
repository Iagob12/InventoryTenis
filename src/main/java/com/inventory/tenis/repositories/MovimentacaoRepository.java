package com.inventory.tenis.repositories;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.inventory.tenis.entities.Movimentacao;
import com.inventory.tenis.entities.StatusMovimentacao;
import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.entities.User;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {
    
    List<Movimentacao> findByUser(User user);
    
    List<Movimentacao> findByUserAndTenis(User user, Tenis tenis);
    
    List<Movimentacao> findByUserAndTipo(User user, StatusMovimentacao tipo);
    
    // ADICIONADO: Método com ordenação por data
    List<Movimentacao> findByUserAndTipoOrderByDataMovimentacaoDesc(User user, StatusMovimentacao tipo);
    
    List<Movimentacao> findByUserAndDataMovimentacaoBetween(User user, Date inicio, Date fim);
    
    // ADICIONADO: Método com ordenação por data
    List<Movimentacao> findByUserAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(User user, Date inicio, Date fim);
    
    List<Movimentacao> findByUserOrderByDataMovimentacaoDesc(User user);
    
    // ADICIONADO: Query personalizada para buscar por inventário
    @Query("SELECT m FROM Movimentacao m JOIN Inventario i ON m.tenis = i.tenis WHERE i.id = :inventarioId ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByInventarioId(@Param("inventarioId") UUID inventarioId);
}