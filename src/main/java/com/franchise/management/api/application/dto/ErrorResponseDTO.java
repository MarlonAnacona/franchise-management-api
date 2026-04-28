package com.franchise.management.api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private String code;
    private String message;
    private LocalDateTime timestamp;

}
