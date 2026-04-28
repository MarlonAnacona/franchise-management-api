package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.*;

public interface BranchUseCase {

    ResponseBranchDTO save(RegisterBranchDTO registerBranchDTO);

    void delete(Long idBranch, Long idProduct);

    UpdateBranchNameDTO update(Long id, UpdateBranchNameDTO updateBranchNameDTO);
}
