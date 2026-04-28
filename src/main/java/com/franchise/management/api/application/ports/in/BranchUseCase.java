package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.BranchDTO;
import com.franchise.management.api.application.dto.RegisterBranchDTO;
import com.franchise.management.api.application.dto.UpdateBranchNameDTO;
import com.franchise.management.api.application.dto.UpdateStockDTO;

public interface BranchUseCase {

    RegisterBranchDTO save(RegisterBranchDTO registerBranchDTO);

    void delete(Long idBranch, Long idProduct);

    UpdateBranchNameDTO update(Long id, UpdateBranchNameDTO updateBranchNameDTO);
}
