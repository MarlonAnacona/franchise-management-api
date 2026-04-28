package com.franchise.management.api.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBranchDTO {
    private Long branchId;
    private String name;

    private Long franchiseId;
}
