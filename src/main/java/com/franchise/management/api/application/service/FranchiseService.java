package com.franchise.management.api.application.service;

import com.franchise.management.api.application.dto.FranchiseDTO;
import com.franchise.management.api.application.dto.RegisterFranchiseDTO;
import com.franchise.management.api.application.dto.TopProductDTO;
import com.franchise.management.api.application.dto.UpdateFranchiseNameDTO;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.application.ports.in.FranchiseUseCase;
import com.franchise.management.api.domain.model.Franchise;
import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.FranchiseRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class FranchiseService implements FranchiseUseCase {

    private final FranchiseRepositoryPort repositoryPort;

    @Autowired
    public FranchiseService(FranchiseRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }



    @Override
    public RegisterFranchiseDTO save(RegisterFranchiseDTO dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.NAME_CANNOT_BE_EMPTY);
        }

        Franchise franchise = Franchise.builder()
                .name(dto.getName())
                .build();

        return toDTO(repositoryPort.save(franchise));
    }

    @Override
    public List<TopProductDTO> findProductTop(Long franchiseId) {


           /* Se hace uso de streams, pero por perfomance se opta mejor por JPQL, evitando así problemas de carga con demasiados datos
            Este stream tiene problema de N+1, por ende no se opta , sin embargo se deja para explicar en la prueba el porque no uso de streams

                    Franchise franchise = repositoryPort.findById(franchiseId)
                    .orElseThrow(()-> new RuntimeException("Franchise not found"));

            return franchise.getBranches()
                    .stream()
                    .map(branch -> {
                        Product topProduct = branch.getProducts()
                                .stream()
                                .max(Comparator.comparing(Product::getStock))
                                .orElse(null);
                        if (topProduct == null) return null;

                        return TopProductDTO.builder().branchName(branch.getName()).productName(topProduct.getName()).stock(topProduct.getStock()).build();
                    })
                    .filter(Objects::nonNull)
                    .toList();
*/

        if (!repositoryPort.existsById(franchiseId)) {
            throw new NotFoundException(ErrorMessages.FRANCHISE_NOT_FOUND);

        }
            return  repositoryPort.findProductTop(franchiseId);

    }

    @Override
    public RegisterFranchiseDTO update(Long id, UpdateFranchiseNameDTO updateFranchiseNameDTO) {
        Franchise franchise= repositoryPort.findById(id).orElseThrow(()->new NotFoundException(ErrorMessages.FRANCHISE_NOT_FOUND));
        if (updateFranchiseNameDTO.getName() == null || updateFranchiseNameDTO.getName().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.NAME_CANNOT_BE_EMPTY);
        }
        franchise.setName(updateFranchiseNameDTO.getName());

        return toDTO(repositoryPort.save(franchise));
    }


    private RegisterFranchiseDTO toDTO(Franchise franchise) {

        return RegisterFranchiseDTO.builder().name(franchise.getName()).build();
    }



}
