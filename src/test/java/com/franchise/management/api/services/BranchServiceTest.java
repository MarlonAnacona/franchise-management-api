package com.franchise.management.api.services;


import com.franchise.management.api.application.dto.RegisterBranchDTO;
import com.franchise.management.api.application.dto.ResponseBranchDTO;
import com.franchise.management.api.application.dto.UpdateBranchNameDTO;
import com.franchise.management.api.application.service.BranchService;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.domain.model.Branch;
import com.franchise.management.api.domain.model.Franchise;
import com.franchise.management.api.domain.model.Product;
import com.franchise.management.api.domain.ports.out.BranchRepositoryPort;
import com.franchise.management.api.domain.ports.out.FranchiseRepositoryPort;
import com.franchise.management.api.domain.ports.out.ProductRepositoryPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private BranchService branchService;


    @Test
    void save_success() {

        RegisterBranchDTO dto = new RegisterBranchDTO();
        dto.setName("North Branch");
        dto.setFranchiseId(1L);

        Franchise franchise = Franchise.builder()
                .id(1L)
                .name("Franchise A")
                .build();

        Branch savedBranch = Branch.builder()
                .id(10L)
                .name("North Branch")
                .franchise(franchise)
                .build();

        when(franchiseRepositoryPort.findById(1L))
                .thenReturn(Optional.of(franchise));

        when(branchRepositoryPort.save(any())).thenReturn(savedBranch);

        ResponseBranchDTO response = branchService.save(dto);

        assertEquals("North Branch", response.getName());
        assertEquals(1L, response.getFranchiseId());
        assertEquals(10L, response.getBranchId());

        verify(branchRepositoryPort).save(any());
    }


    @Test
    void save_shouldThrow_whenFranchiseNotFound() {

        RegisterBranchDTO dto = new RegisterBranchDTO();
        dto.setFranchiseId(99L);

        when(franchiseRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> branchService.save(dto));

        assertEquals(ErrorMessages.FRANCHISE_NOT_FOUND, ex.getMessage());
    }


    @Test
    void delete_success() {

        Long branchId = 1L;
        Long productId = 100L;

        Branch branch = Branch.builder()
                .id(branchId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .branch(branch)
                .build();

        when(branchRepositoryPort.findById(branchId))
                .thenReturn(Optional.of(branch));

        when(productRepositoryPort.findById(productId))
                .thenReturn(Optional.of(product));

        doNothing().when(productRepositoryPort).delete(branchId, productId);

        branchService.delete(branchId, productId);

        verify(productRepositoryPort).delete(branchId, productId);
    }


    @Test
    void delete_shouldThrow_whenProductNotFound() {

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.of(new Branch()));

        when(productRepositoryPort.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> branchService.delete(1L, 1L));

        assertEquals(ErrorMessages.PRODUCT_NOT_FOUND, ex.getMessage());
    }


    @Test
    void delete_shouldThrow_whenProductNotBelongsToBranch() {

        Branch branch = Branch.builder().id(1L).build();

        Branch otherBranch = Branch.builder().id(2L).build();

        Product product = Product.builder()
                .id(1L)
                .branch(otherBranch)
                .build();

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.of(branch));

        when(productRepositoryPort.findById(1L))
                .thenReturn(Optional.of(product));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> branchService.delete(1L, 1L));

        assertEquals(ErrorMessages.PRODUCT_DOES_NOT_BELONG_TO_THIS_BRANCH, ex.getMessage());
    }


    @Test
    void update_success() {

        Branch branch = Branch.builder()
                .id(1L)
                .name("Old Name")
                .build();

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.of(branch));

        when(branchRepositoryPort.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        UpdateBranchNameDTO request = new UpdateBranchNameDTO();
        request.setName("New Name");

        UpdateBranchNameDTO response = branchService.update(1L, request);

        assertEquals("New Name", response.getName());
    }


    @Test
    void update_shouldThrow_whenNameIsBlank() {

        Branch branch = Branch.builder().id(1L).build();

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.of(branch));

        UpdateBranchNameDTO request = new UpdateBranchNameDTO();
        request.setName(" ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> branchService.update(1L, request));

        assertEquals(ErrorMessages.NAME_CANNOT_BE_EMPTY, ex.getMessage());
    }


    @Test
    void update_shouldThrow_whenBranchNotFound() {

        when(branchRepositoryPort.findById(1L))
                .thenReturn(Optional.empty());

        UpdateBranchNameDTO request = new UpdateBranchNameDTO();
        request.setName("New Name");

        assertThrows(NotFoundException.class,
                () -> branchService.update(1L, request));
    }
}