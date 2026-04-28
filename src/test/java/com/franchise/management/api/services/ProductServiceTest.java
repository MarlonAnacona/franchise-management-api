package com.franchise.management.api.services;

import com.franchise.management.api.application.dto.*;
import com.franchise.management.api.application.service.ProductService;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.domain.model.Branch;
import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.BranchRepositoryPort;
import com.franchise.management.api.domain.ports.out.ProductRepositoryPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @InjectMocks
    private ProductService productService;

    @Test
    void save_success() {

        RegisterProductDTO dto = new RegisterProductDTO();
        dto.setName("Coca Cola");
        dto.setStock(100);
        dto.setBranchId(1L);

        Branch branch = Branch.builder()
                .id(1L)
                .name("North Branch")
                .build();

        Product saved = Product.builder()
                .id(10L)
                .name("Coca Cola")
                .stock(100)
                .branch(branch)
                .build();

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.of(branch));

        when(productRepositoryPort.save(any())).thenReturn(saved);

        ResponseProductDTO response = productService.save(dto);

        assertEquals("Coca Cola", response.getName());
        assertEquals(100, response.getStock());
        assertEquals(1L, response.getBranchId());
        assertEquals(10L, response.getProductId());

        verify(productRepositoryPort).save(any());
    }


    @Test
    void save_shouldThrow_whenBranchNotFound() {

        RegisterProductDTO dto = new RegisterProductDTO();
        dto.setBranchId(99L);

        when(branchRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> productService.save(dto)
        );

        assertEquals(ErrorMessages.BRANCH_NOT_FOUND, ex.getMessage());
    }


    @Test
    void updateStock_success() {

        Long id = 1L;

        Branch branch = Branch.builder().id(1L).build();

        Product product = Product.builder()
                .id(id)
                .name("Coke")
                .stock(10)
                .branch(branch)
                .build();

        when(productRepositoryPort.findById(id))
                .thenReturn(Optional.of(product));

        when(productRepositoryPort.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setStock(50);

        ProductDTO response = productService.updateStock(id, dto);

        assertEquals(50, response.getStock());
        assertEquals("Coke", response.getName());

        verify(productRepositoryPort).save(any());
    }


    @Test
    void updateStock_shouldThrow_whenNegativeStock() {

        Long id = 1L;

        Branch branch = Branch.builder().id(1L).build();

        Product product = Product.builder()
                .id(id)
                .stock(10)
                .branch(branch)
                .build();

        when(productRepositoryPort.findById(id))
                .thenReturn(Optional.of(product));

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setStock(-5);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> productService.updateStock(id, dto)
        );

        assertEquals(ErrorMessages.STOCK_CANNOT_BE_NEGATIVE, ex.getMessage());
    }


    @Test
    void updateName_success() {

        Long id = 1L;

        Branch branch = Branch.builder().id(1L).build();

        Product product = Product.builder()
                .id(id)
                .name("Coke")
                .stock(10)
                .branch(branch)
                .build();

        when(productRepositoryPort.findById(id))
                .thenReturn(Optional.of(product));

        when(productRepositoryPort.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateProductNameDTO dto = new UpdateProductNameDTO();
        dto.setName("Pepsi");

        ProductDTO response = productService.updateName(id, dto);

        assertEquals("Pepsi", response.getName());
        assertEquals(10, response.getStock());
    }


    @Test
    void updateName_shouldThrow_whenNameBlank() {

        Long id = 1L;

        Branch branch = Branch.builder().id(1L).build();

        Product product = Product.builder()
                .id(id)
                .name("Coke")
                .stock(10)
                .branch(branch)
                .build();

        when(productRepositoryPort.findById(id))
                .thenReturn(Optional.of(product));

        UpdateProductNameDTO dto = new UpdateProductNameDTO();
        dto.setName(" ");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateName(id, dto)
        );

        assertEquals(ErrorMessages.NAME_CANNOT_BE_EMPTY, ex.getMessage());
    }


    @Test
    void update_shouldThrow_whenProductNotFound() {

        when(productRepositoryPort.findById(1L))
                .thenReturn(Optional.empty());

        UpdateStockDTO dto = new UpdateStockDTO();
        dto.setStock(10);

        assertThrows(NotFoundException.class,
                () -> productService.updateStock(1L, dto));
    }
}