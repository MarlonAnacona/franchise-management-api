package com.franchise.management.api.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franchise.management.api.application.dto.*;
import com.franchise.management.api.application.ports.in.FranchiseUseCase;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.entrypoints.controller.FranchiseController;

import com.franchise.management.api.entrypoints.controller.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FranchiseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FranchiseUseCase franchiseUseCase;

    @InjectMocks
    private FranchiseController franchiseController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(franchiseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void save_success() throws Exception {

        RegisterFranchiseDTO request = new RegisterFranchiseDTO();
        ResponseFranchiseDTO responseMock = new ResponseFranchiseDTO();

        when(franchiseUseCase.save(any())).thenReturn(responseMock);

        mockMvc.perform(post("/v1/franchise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getMaxProductStock_success() throws Exception {

        Long id = 1L;

        TopProductDTO responseMock = new TopProductDTO();
        responseMock.setProductName("papas");
        responseMock.setBranchName("roosvelt");
        responseMock.setStock(20);
        List<TopProductDTO> response= new ArrayList<>();
        response.add(responseMock);
        when(franchiseUseCase.findProductTop(id)).thenReturn(response);

        mockMvc.perform(get("/v1/franchise/{id}/branches/top", id))
                .andExpect(status().isOk());
    }


    @Test
    void update_success() throws Exception {

        Long id = 1L;

        UpdateFranchiseNameDTO request = new UpdateFranchiseNameDTO();
        RegisterFranchiseDTO responseMock = new RegisterFranchiseDTO();

        when(franchiseUseCase.update(eq(id), any())).thenReturn(responseMock);

        mockMvc.perform(patch("/v1/franchise/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void getTop_shouldReturn404_whenNotFound() throws Exception {

        Long id = 1L;

        when(franchiseUseCase.findProductTop(id))
                .thenThrow(new NotFoundException("Franchise not found"));

        mockMvc.perform(get("/v1/franchise/{id}/branches/top", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Franchise not found"));
    }


    @Test
    void update_shouldReturn409_whenBusinessError() throws Exception {

        Long id = 1L;

        UpdateFranchiseNameDTO request = new UpdateFranchiseNameDTO();

        when(franchiseUseCase.update(eq(id), any()))
                .thenThrow(new BusinessException("Invalid franchise name"));

        mockMvc.perform(patch("/v1/franchise/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }
}