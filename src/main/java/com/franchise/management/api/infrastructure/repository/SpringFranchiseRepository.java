package com.franchise.management.api.infrastructure.repository;

import com.franchise.management.api.application.dto.TopProductDTO;
import com.franchise.management.api.domain.model.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringFranchiseRepository  extends JpaRepository<Franchise,Long> {

    @Query(""" 
        SELECT new com.franchise.management.api.application.dto.TopProductDTO(
        b.name,
        p.name,
        p.stock
        )
        from Branch b
        JOIN b.products p
        WHERE b.franchise.id = :franchiseId
        AND p.stock = (
            SELECT MAX(p2.stock)
            FROM Product p2
            WHERE p2.branch.id = b.id
        )
    """)
    List<TopProductDTO> findTopProductsByFranchise(Long franchiseId);
}
