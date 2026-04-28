package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.ProductDTO;
import com.franchise.management.api.application.dto.RegisterProductDTO;
import com.franchise.management.api.application.dto.UpdateProductNameDTO;
import com.franchise.management.api.application.dto.UpdateStockDTO;

public interface ProductUseCase {

    RegisterProductDTO save(RegisterProductDTO registerProductDTO);

    ProductDTO updateStock(Long id, UpdateStockDTO updateStockDTO);

    ProductDTO updateName(Long id, UpdateProductNameDTO updateProductNameDTO);

}
