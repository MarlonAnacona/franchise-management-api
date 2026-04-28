package com.franchise.management.api.infrastructure.adapter;

import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.ProductRepositoryPort;
import com.franchise.management.api.infrastructure.repository.SpringProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {


    private SpringProductRepository springProductRepository;

    @Autowired
    public ProductRepositoryAdapter(SpringProductRepository springProductRepository) {
        this.springProductRepository = springProductRepository;
    }

    @Override
    public Product save(Product product) {
        return springProductRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springProductRepository.findById(id);
    }

    public void delete(Long idBranch, Long idProduct) {
        springProductRepository.deleteByBranch_IdAndId(idBranch,idProduct);
    }

}
