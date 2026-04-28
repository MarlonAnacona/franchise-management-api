package com.franchise.management.api.infrastructure.adapter;

import com.franchise.management.api.application.ports.in.BranchUseCase;
import com.franchise.management.api.domain.model.Branch;
import com.franchise.management.api.domain.ports.out.BranchRepositoryPort;
import com.franchise.management.api.infrastructure.repository.SpringBranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private SpringBranchRepository springBranchRepository;

    @Autowired
    public BranchRepositoryAdapter(SpringBranchRepository springBranchRepository) {
        this.springBranchRepository = springBranchRepository;
    }

    @Override
    public Branch save(Branch branch) {
        return springBranchRepository.save(branch);
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return springBranchRepository.findById(id);
    }


}
