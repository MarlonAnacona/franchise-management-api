package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.*;

import java.util.List;

public interface FranchiseUseCase {



    ResponseFranchiseDTO save(RegisterFranchiseDTO dto);

    List<TopProductDTO> findProductTop(Long id);

    RegisterFranchiseDTO update(Long id, UpdateFranchiseNameDTO updateFranchiseNameDTO);
}
