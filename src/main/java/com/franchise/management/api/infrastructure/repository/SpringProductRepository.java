package com.franchise.management.api.infrastructure.repository;

import com.franchise.management.api.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringProductRepository extends JpaRepository<Product, Long> {
    void deleteByBranch_IdAndId(Long branchId, Long productId);
}
