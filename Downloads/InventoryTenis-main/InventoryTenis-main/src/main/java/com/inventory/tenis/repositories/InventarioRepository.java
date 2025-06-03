package com.inventory.tenis.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.tenis.entities.Inventario;
import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.entities.User;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, UUID> {
    
    List<Inventario> findByUser(User user);
    
    List<Inventario> findByUserAndQuantidadeGreaterThan(User user, Integer quantidade);
    
    Optional<Inventario> findByUserAndTenis(User user, Tenis tenis);
    
    Long countByUserAndQuantidadeGreaterThan(User user, Integer quantidade);
}