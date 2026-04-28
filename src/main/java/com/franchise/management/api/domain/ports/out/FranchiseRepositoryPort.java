package com.franchise.management.api.domain.ports.out;

import com.franchise.management.api.application.dto.TopProductDTO;
import com.franchise.management.api.domain.model.Franchise;

import java.util.List;
import java.util.Optional;

public interface FranchiseRepositoryPort {



    Optional<Franchise> findById(Long id);

    Franchise save(Franchise franchise);

    List<TopProductDTO> findProductTop(Long id);
}
