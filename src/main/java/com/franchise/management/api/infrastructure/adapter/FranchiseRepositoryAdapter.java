package com.franchise.management.api.infrastructure.adapter;

import com.franchise.management.api.application.dto.TopProductDTO;
import com.franchise.management.api.domain.model.Franchise;
import com.franchise.management.api.domain.ports.out.FranchiseRepositoryPort;
import com.franchise.management.api.infrastructure.repository.SpringFranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final SpringFranchiseRepository repository;


    @Override
    public Optional<Franchise> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Franchise save(Franchise franchise) {
        return repository.save(franchise);
    }

    @Override
    public List<TopProductDTO> findProductTop(Long id) {
        return repository.findTopProductsByFranchise(id);
    }

    @Override
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }


}
