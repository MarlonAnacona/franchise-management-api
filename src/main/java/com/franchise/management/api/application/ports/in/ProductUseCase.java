package com.franchise.management.api.application.ports.in;

import com.franchise.management.api.application.dto.*;

public interface ProductUseCase {

    ResponseProductDTO save(RegisterProductDTO registerProductDTO);

    ProductDTO updateStock(Long id, UpdateStockDTO updateStockDTO);

    ProductDTO updateName(Long id, UpdateProductNameDTO updateProductNameDTO);

}
