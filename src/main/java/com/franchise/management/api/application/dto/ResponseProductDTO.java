package com.franchise.management.api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductDTO {

    private Long productId;
    private String name;
    private Long branchId;
    private int stock;


}
