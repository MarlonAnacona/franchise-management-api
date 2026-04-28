package com.franchise.management.api.entrypoints.controller;

import com.franchise.management.api.application.dto.RegisterProductDTO;
import com.franchise.management.api.application.dto.UpdateProductNameDTO;
import com.franchise.management.api.application.dto.UpdateStockDTO;
import com.franchise.management.api.application.ports.in.ProductUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductUseCase productUseCase;

    @Autowired
    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody RegisterProductDTO request){

        return ResponseEntity.ok(productUseCase.save(request));
    }



    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable Long id, @RequestBody UpdateStockDTO updateStockDTO) {


        return ResponseEntity.ok(productUseCase.updateStock(id,updateStockDTO));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateName(
            @PathVariable Long id, @RequestBody UpdateProductNameDTO productNameDTO) {


        return ResponseEntity.ok(productUseCase.updateName(id,productNameDTO));
    }


}
