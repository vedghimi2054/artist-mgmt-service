
package com.company.artistmgmt.endpoint;

import com.company.artistmgmt.exception.ArtistException;
import com.company.artistmgmt.model.BaseResponse;
import com.company.artistmgmt.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ARTIST_MANAGER','ARTIST')")
    public ResponseEntity<BaseResponse<List<Object>>> getAllDashboardStats() {
        try {
            BaseResponse<List<Object>> response = dashboardService.getAllDashboardStats();
            return ResponseEntity.ok(response);
        } catch (ArtistException ex) {
            BaseResponse<List<Object>> errorResponse = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

}

