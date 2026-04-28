package com.franchise.management.api.controllertest;

import com.franchise.management.api.application.ports.in.BranchUseCase;
import com.franchise.management.api.entrypoints.controller.BranchController;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BranchControllerTest {

    @Mock
    private BranchUseCase branchUseCase;

    @InjectMocks
    private BranchController branchController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
}
