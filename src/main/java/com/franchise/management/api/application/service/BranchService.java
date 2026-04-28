package com.franchise.management.api.application.service;

import com.franchise.management.api.application.dto.BranchDTO;
import com.franchise.management.api.application.dto.RegisterBranchDTO;
import com.franchise.management.api.application.dto.ResponseBranchDTO;
import com.franchise.management.api.application.dto.UpdateBranchNameDTO;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.application.ports.in.BranchUseCase;
import com.franchise.management.api.domain.model.Branch;
import com.franchise.management.api.domain.model.Franchise;
import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.BranchRepositoryPort;
import com.franchise.management.api.domain.ports.out.FranchiseRepositoryPort;
import com.franchise.management.api.domain.ports.out.ProductRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService implements BranchUseCase {

    private BranchRepositoryPort repositoryPort;

    private FranchiseRepositoryPort franchiseRepositoryPort;

    private ProductRepositoryPort productRepositoryPort;

    @Autowired
    public BranchService(BranchRepositoryPort repositoryPort, FranchiseRepositoryPort franchiseRepositoryPort, ProductRepositoryPort productRepositoryPort) {
        this.repositoryPort = repositoryPort;
        this.franchiseRepositoryPort = franchiseRepositoryPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ResponseBranchDTO save(RegisterBranchDTO registerBranchDTO) {

        Franchise franchise = this.franchiseRepositoryPort.findById(registerBranchDTO.getFranchiseId())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.FRANCHISE_NOT_FOUND));

        Branch branch = Branch.builder()
                .name(registerBranchDTO.getName())
                .franchise(franchise)
                .build();


         return toDTO(repositoryPort.save(branch));


    }

    @Override
    public void delete(Long idBranch, Long idProduct) {

        repositoryPort.findById(idBranch)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.FRANCHISE_NOT_FOUND));

        Product product = productRepositoryPort.findById(idProduct)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));

        if (!product.getBranch().getId().equals(idBranch)) {
            throw new BusinessException(ErrorMessages.PRODUCT_DOES_NOT_BELONG_TO_THIS_BRANCH);
        }
         productRepositoryPort.delete(idBranch,idProduct);
    }

    @Override
    public UpdateBranchNameDTO update(Long id, UpdateBranchNameDTO updateBranchNameDTO) {
        Branch branch= repositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.BRANCH_NOT_FOUND));
        if (updateBranchNameDTO.getName() == null || updateBranchNameDTO.getName().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.NAME_CANNOT_BE_EMPTY);
        }
        branch.setName(updateBranchNameDTO.getName());

        return toUpdateDTO(repositoryPort.save(branch));
    }

    private ResponseBranchDTO toDTO(Branch branch) {

        return ResponseBranchDTO.builder().name(branch.getName()).franchiseId(branch.getFranchise().getId()).branchId(branch.getId()).build();
    }

    private UpdateBranchNameDTO toUpdateDTO(Branch branch) {

        return UpdateBranchNameDTO.builder().name(branch.getName()).build();
    }

}
