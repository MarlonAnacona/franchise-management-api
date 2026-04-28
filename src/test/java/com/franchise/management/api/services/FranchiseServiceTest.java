package com.franchise.management.api.services;


import com.franchise.management.api.application.dto.*;
import com.franchise.management.api.application.service.FranchiseService;
import com.franchise.management.api.domain.constants.ErrorMessages;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.domain.model.Franchise;
import com.franchise.management.api.domain.ports.out.FranchiseRepositoryPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort repositoryPort;

    @InjectMocks
    private FranchiseService franchiseService;

    @Test
    void save_success() {

        RegisterFranchiseDTO dto = new RegisterFranchiseDTO();
        dto.setName("Franchise Colombia");

        Franchise saved = Franchise.builder()
                .id(1L)
                .name("Franchise Colombia")
                .build();

        when(repositoryPort.save(any())).thenReturn(saved);

        ResponseFranchiseDTO response = franchiseService.save(dto);

        assertEquals("Franchise Colombia", response.getName());
        assertEquals(1L, response.getFranchiseId());

        verify(repositoryPort).save(any());
    }


    @Test
    void save_shouldThrow_whenNameIsEmpty() {

        RegisterFranchiseDTO dto = new RegisterFranchiseDTO();
        dto.setName(" ");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> franchiseService.save(dto)
        );

        assertEquals(ErrorMessages.NAME_CANNOT_BE_EMPTY, ex.getMessage());
    }

    @Test
    void findProductTop_success() {

        Long franchiseId = 1L;

        when(repositoryPort.existsById(franchiseId)).thenReturn(true);

        List<TopProductDTO> expected = List.of(
                TopProductDTO.builder()
                        .branchName("North Branch")
                        .productName("Coke")
                        .stock(50)
                        .build()
        );

        when(repositoryPort.findProductTop(franchiseId)).thenReturn(expected);

        List<TopProductDTO> result = franchiseService.findProductTop(franchiseId);

        assertEquals(1, result.size());
        assertEquals("North Branch", result.get(0).getBranchName());
        assertEquals("Coke", result.get(0).getProductName());
        assertEquals(50, result.get(0).getStock());

        verify(repositoryPort).findProductTop(franchiseId);
    }

    @Test
    void findProductTop_shouldThrow_whenFranchiseNotFound() {

        Long franchiseId = 99L;

        when(repositoryPort.existsById(franchiseId)).thenReturn(false);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> franchiseService.findProductTop(franchiseId)
        );

        assertEquals(ErrorMessages.FRANCHISE_NOT_FOUND, ex.getMessage());
    }


    @Test
    void update_success() {

        Long id = 1L;

        Franchise franchise = Franchise.builder()
                .id(id)
                .name("Old Name")
                .build();

        when(repositoryPort.findById(id)).thenReturn(Optional.of(franchise));

        when(repositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateFranchiseNameDTO dto = new UpdateFranchiseNameDTO();
        dto.setName("New Franchise Name");

        RegisterFranchiseDTO result = franchiseService.update(id, dto);

        assertEquals("New Franchise Name", result.getName());

        verify(repositoryPort).save(any());
    }


    @Test
    void update_shouldThrow_whenNotFound() {

        Long id = 1L;

        when(repositoryPort.findById(id)).thenReturn(Optional.empty());

        UpdateFranchiseNameDTO dto = new UpdateFranchiseNameDTO();
        dto.setName("New Name");

        assertThrows(NotFoundException.class,
                () -> franchiseService.update(id, dto));
    }


    @Test
    void update_shouldThrow_whenNameIsBlank() {

        Long id = 1L;

        Franchise franchise = Franchise.builder()
                .id(id)
                .name("Old Name")
                .build();

        when(repositoryPort.findById(id)).thenReturn(Optional.of(franchise));

        UpdateFranchiseNameDTO dto = new UpdateFranchiseNameDTO();
        dto.setName(" ");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> franchiseService.update(id, dto)
        );

        assertEquals(ErrorMessages.NAME_CANNOT_BE_EMPTY, ex.getMessage());
    }
}