package com.franchise.management.api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TopProductDTO {
    private String branchName;
    private String productName;
    private Integer stock;
}
