package com.franchise.management.api.application.service;

import com.franchise.management.api.application.dto.RegisterBranchDTO;
import com.franchise.management.api.application.dto.UpdateBranchNameDTO;
import com.franchise.management.api.domain.constants.ErrorMessages;
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
    public RegisterBranchDTO save(RegisterBranchDTO registerBranchDTO) {

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
            throw new RuntimeException("Product does not belong to this branch");
        }
         productRepositoryPort.delete(idBranch,idProduct);
    }

    @Override
    public UpdateBranchNameDTO update(Long id, UpdateBranchNameDTO updateBranchNameDTO) {
        Branch branch= repositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.BRANCH_NOT_FOUND));

        branch.setName(updateBranchNameDTO.getName());

        return toUpdateDTO(repositoryPort.save(branch));
    }

    private RegisterBranchDTO toDTO(Branch branch) {

        return RegisterBranchDTO.builder().name(branch.getName()).franchiseId(branch.getFranchise().getId()).build();
    }

    private UpdateBranchNameDTO toUpdateDTO(Branch branch) {

        return UpdateBranchNameDTO.builder().name(branch.getName()).build();
    }

}
