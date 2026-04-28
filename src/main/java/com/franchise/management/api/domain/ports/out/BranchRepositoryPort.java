package com.franchise.management.api.domain.ports.out;

import com.franchise.management.api.domain.model.Branch;

import java.util.Optional;

public interface BranchRepositoryPort {


    Branch save (Branch branch);

    Optional<Branch> findById(Long id);


}
