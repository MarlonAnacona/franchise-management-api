package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.FranchiseDTO;
import com.franchise.management.api.application.dto.RegisterFranchiseDTO;
import com.franchise.management.api.application.dto.TopProductDTO;
import com.franchise.management.api.application.dto.UpdateFranchiseNameDTO;

import java.util.List;

public interface FranchiseUseCase {
    List<FranchiseDTO> findAll();

    FranchiseDTO findById(Long id);

    RegisterFranchiseDTO save(RegisterFranchiseDTO dto);

    List<TopProductDTO> findProductTop(Long id);

    RegisterFranchiseDTO update(Long id, UpdateFranchiseNameDTO updateFranchiseNameDTO);
}
