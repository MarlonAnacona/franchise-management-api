package com.franchise.management.api.application.service;

import com.franchise.management.api.application.dto.ProductDTO;
import com.franchise.management.api.application.dto.RegisterProductDTO;
import com.franchise.management.api.application.dto.UpdateProductNameDTO;
import com.franchise.management.api.application.dto.UpdateStockDTO;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.application.ports.in.ProductUseCase;
import com.franchise.management.api.domain.model.Branch;
import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.BranchRepositoryPort;
import com.franchise.management.api.domain.ports.out.ProductRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort repositoryPort;

    private final BranchRepositoryPort repositoryPortBranch;

    @Autowired
    public ProductService(ProductRepositoryPort repositoryPort, BranchRepositoryPort repositoryPortBranch) {
        this.repositoryPort = repositoryPort;
        this.repositoryPortBranch = repositoryPortBranch;
    }

    @Override
    public RegisterProductDTO save(RegisterProductDTO registerProductDTO) {

        Branch branch=repositoryPortBranch.findById(registerProductDTO.getBranchId()).orElseThrow(() -> new NotFoundException(ErrorMessages.BRANCH_NOT_FOUND));
        Product product= Product.builder()
                .name(registerProductDTO.getName())
                .stock(registerProductDTO.getStock())
                .branch(branch)
                .build();
        return toDtoProductRegister(repositoryPort.save(product));
    }

    @Override
    public ProductDTO updateStock(Long id, UpdateStockDTO updateStockDTO) {

        Product product = findProduct(id);
        if (updateStockDTO.getStock() < 0) {
            throw new BusinessException(ErrorMessages.STOCK_CANNOT_BE_NEGATIVE);
        }
        product.setStock(updateStockDTO.getStock());

        return toDtoProduct(repositoryPort.save(product));
    }

    @Override
    public ProductDTO updateName(Long id, UpdateProductNameDTO updateProductNameDTO) {
        Product product = findProduct(id);
        if (updateProductNameDTO.getName() == null || updateProductNameDTO.getName().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.NAME_CANNOT_BE_EMPTY);
        }

        product.setName(updateProductNameDTO.getName());

        return toDtoProduct(repositoryPort.save(product));
    }

    private Product findProduct(Long id) {
        return repositoryPort.findById(id).orElseThrow(()->new NotFoundException(ErrorMessages.PRODUCT_NOT_FOUND));
    }


    public RegisterProductDTO toDtoProductRegister(Product product){

        return RegisterProductDTO.builder().name(product.getName()).stock(product.getStock()).branchId(product.getBranch().getId()).build();
    }

    public ProductDTO toDtoProduct(Product product){
        return ProductDTO.builder().name(product.getName()).stock(product.getStock()).build();
    }

}
