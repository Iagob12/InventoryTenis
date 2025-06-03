// TenisRepository.java - Adicione este método
package com.inventory.tenis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventory.tenis.entities.Tenis;
import com.inventory.tenis.entities.User;

@Repository
public interface TenisRepository extends JpaRepository<Tenis, Long> {
    
    List<Tenis> findByUser(User user);
    
    List<Tenis> findByUserOrderByNome(User user);
    
    Long countByUser(User user);
}