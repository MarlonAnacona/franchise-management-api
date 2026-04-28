package com.franchise.management.api.infrastructure.repository;

import com.franchise.management.api.domain.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringBranchRepository extends JpaRepository<Branch,Long> {

}
