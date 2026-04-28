package com.franchise.management.api.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franchise.management.api.application.dto.*;
import com.franchise.management.api.application.ports.in.ProductUseCase;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.entrypoints.controller.ProductController;

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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void save_success() throws Exception {

        RegisterProductDTO request = new RegisterProductDTO();
        ResponseProductDTO responseMock = new ResponseProductDTO();

        when(productUseCase.save(any())).thenReturn(responseMock);

        mockMvc.perform(post("/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void updateStock_success() throws Exception {

        Long id = 1L;

        UpdateStockDTO request = new UpdateStockDTO();
        ProductDTO responseMock = new ProductDTO();

        when(productUseCase.updateStock(eq(id), any())).thenReturn(responseMock);

        mockMvc.perform(patch("/v1/product/{id}/stock", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void updateName_success() throws Exception {

        Long id = 1L;

        UpdateProductNameDTO request = new UpdateProductNameDTO();
        ProductDTO responseMock = new ProductDTO();

        when(productUseCase.updateName(eq(id), any())).thenReturn(responseMock);

        mockMvc.perform(patch("/v1/product/{id}/name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void updateStock_shouldReturn404_whenNotFound() throws Exception {

        Long id = 1L;

        UpdateStockDTO request = new UpdateStockDTO();

        when(productUseCase.updateStock(eq(id), any()))
                .thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(patch("/v1/product/{id}/stock", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }


    @Test
    void updateName_shouldReturn409_whenBusinessError() throws Exception {

        Long id = 1L;

        UpdateProductNameDTO request = new UpdateProductNameDTO();

        when(productUseCase.updateName(eq(id), any()))
                .thenThrow(new BusinessException("Invalid product name"));

        mockMvc.perform(patch("/v1/product/{id}/name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }
}