package com.franchise.management.api.domain.ports.out;

import com.franchise.management.api.domain.model.Product;

import java.util.Optional;

public interface ProductRepositoryPort {


    Product save (Product product);

    Optional<Product> findById(Long id);

    void delete (Long idBranch,Long idProduct);

}
