package com.franchise.management.api.entrypoints.controller;

import com.franchise.management.api.application.dto.RegisterFranchiseDTO;
import com.franchise.management.api.application.dto.UpdateFranchiseNameDTO;
import com.franchise.management.api.application.ports.in.FranchiseUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/franchise")
@CrossOrigin("*")
public class FranchiseController {


    private final FranchiseUseCase franchiseUseCase;

    public FranchiseController(FranchiseUseCase franchiseUseCase) {
        this.franchiseUseCase = franchiseUseCase;
    }

    @PostMapping()
    public ResponseEntity<?> save( @RequestBody RegisterFranchiseDTO request){

        return ResponseEntity.ok(franchiseUseCase.save(request));
    }

    @GetMapping("/{id}/branches/top")
    public ResponseEntity<?> getMaxProductStock(@PathVariable Long id){

        return ResponseEntity.ok(franchiseUseCase.findProductTop(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id, @RequestBody UpdateFranchiseNameDTO updateFranchiseNameDTO) {


        return ResponseEntity.ok(franchiseUseCase.update(id, updateFranchiseNameDTO));
    }


}
