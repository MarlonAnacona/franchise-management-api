package com.franchise.management.api.entrypoints.controller;

import com.franchise.management.api.application.dto.RegisterBranchDTO;
import com.franchise.management.api.application.dto.RegisterFranchiseDTO;
import com.franchise.management.api.application.dto.UpdateBranchNameDTO;
import com.franchise.management.api.application.dto.UpdateFranchiseNameDTO;
import com.franchise.management.api.application.ports.in.BranchUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/branch")
@CrossOrigin("*")
public class BranchController {

    private final BranchUseCase branchUseCase;

    @Autowired
    public BranchController(BranchUseCase branchUseCase) {
        this.branchUseCase = branchUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody RegisterBranchDTO request){

        return ResponseEntity.ok(branchUseCase.save(request));
    }


    @DeleteMapping("/{branchId}/products/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long branchId,
            @PathVariable Long productId) {

       branchUseCase.delete(branchId,productId);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id, @RequestBody UpdateBranchNameDTO updateBranchNameDTO) {


        return ResponseEntity.ok(branchUseCase.update(id, updateBranchNameDTO));
    }
}
