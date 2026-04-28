package com.franchise.management.api.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franchise.management.api.application.dto.*;
import com.franchise.management.api.application.ports.in.BranchUseCase;
import com.franchise.management.api.domain.exception.BusinessException;
import com.franchise.management.api.domain.exception.NotFoundException;
import com.franchise.management.api.entrypoints.controller.BranchController;
import com.franchise.management.api.entrypoints.controller.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BranchControllerTest {


    @ExtendWith(MockitoExtension.class)
    @Nested
    class UnitTests {

        @Mock
        private BranchUseCase branchUseCase;

        @InjectMocks
        private BranchController branchController;

        @Test
        void save_success() {

            RegisterBranchDTO request = new RegisterBranchDTO();
            request.setName("mc donadl´s");
            request.setFranchiseId(1L);
            ResponseBranchDTO responseMock = new ResponseBranchDTO();
            responseMock.setName("mc donadl´s");
            responseMock.setFranchiseId(1L);
            responseMock.setBranchId(1L);
            when(branchUseCase.save(request)).thenReturn(responseMock);

            var response = branchController.save(request);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(responseMock, response.getBody());

            verify(branchUseCase).save(request);
        }

        @Test
        void delete_success() {

            doNothing().when(branchUseCase).delete(1L, 2L);

            var response = branchController.delete(1L, 2L);

            assertEquals(202, response.getStatusCodeValue());
            verify(branchUseCase).delete(1L, 2L);
        }

        @Test
        void update_success() {

            UpdateBranchNameDTO dto = new UpdateBranchNameDTO();
            UpdateBranchNameDTO responseMock = new UpdateBranchNameDTO();

            when(branchUseCase.update(eq(1L), any())).thenReturn(responseMock);

            var response = branchController.update(1L, dto);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(responseMock, response.getBody());
        }
    }


    @Nested
    class WebTests {

        private MockMvc mockMvc;

        @Mock
        private BranchUseCase branchUseCase;

        @InjectMocks
        private BranchController branchController;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        void setup() {

            org.mockito.MockitoAnnotations.openMocks(this);

            mockMvc = MockMvcBuilders
                    .standaloneSetup(branchController)
                    .setControllerAdvice(new GlobalExceptionHandler())
                    .build();
        }

        @Test
        void delete_shouldReturn404_whenNotFound() throws Exception {

            doThrow(new NotFoundException("Branch not found"))
                    .when(branchUseCase).delete(1L, 2L);

            mockMvc.perform(delete("/v1/branch/{branchId}/products/{productId}", 1L, 2L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Branch not found"));
        }

        @Test
        void update_shouldReturn409_whenBusinessError() throws Exception {

            UpdateBranchNameDTO dto = new UpdateBranchNameDTO();

            doThrow(new BusinessException("Invalid name"))
                    .when(branchUseCase).update(eq(1L), any());

            mockMvc.perform(patch("/v1/branch/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("CONFLICT"));
        }

        @Test
        void save_shouldReturn200() throws Exception {

            RegisterBranchDTO request = new RegisterBranchDTO();
            ResponseBranchDTO responseMock = new ResponseBranchDTO();

            when(branchUseCase.save(any())).thenReturn(responseMock);

            mockMvc.perform(post("/v1/branch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }
}