package com.sogeti.leaseservice.controller;
import com.sogeti.leaseservice.dto.LeaseDTO;
import com.sogeti.leaseservice.service.LeaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Management", description = "Car Management APIs")
@RestController
@RequestMapping("/api/lease")
public class LeaseController {

    private final LeaseService leaseService;

    @Autowired
    public LeaseController(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    @Operation(summary = "Receive All Lease Details", security = @SecurityRequirement(name = "bearerToken"), description = "Receive All Car Details after authentication using JWT token")
    @GetMapping
    public ResponseEntity<List<LeaseDTO>> getAllLeases(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && leaseService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(leaseService.getAllLeases());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Create Lease In Lease Database", security = @SecurityRequirement(name = "bearerToken"), description = "Create lease after authentication using JWT token")
    @PostMapping
    public ResponseEntity<LeaseDTO> createLease(@RequestBody LeaseDTO leaseDTO, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            leaseService.validateCustomer(leaseDTO.getCustomerId(), token);
            leaseService.validateCar(leaseDTO.getCarId(), token);
            return ResponseEntity.status(HttpStatus.OK).body(leaseService.createLease(leaseDTO));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get specific lease details", security = @SecurityRequirement(name = "bearerToken"), description = "Get lease details after authentication using JWT token")
    @GetMapping("/{id}")
    public ResponseEntity<LeaseDTO> getCarById(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && leaseService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(leaseService.getLeaseById(id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Update specific lease details", security = @SecurityRequirement(name = "bearerToken"), description = "Update lease details after authentication using JWT token")
    @PutMapping("/{id}")
    public ResponseEntity<LeaseDTO> updateCar(@PathVariable Long id, @RequestBody LeaseDTO leaseDTO, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && leaseService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(leaseService.updateLease(id, leaseDTO));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Delete specific lease", security = @SecurityRequirement(name = "bearerToken"), description = "Delete lease after authentication using JWT token")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLease(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && leaseService.isValidToken(token)) {
            leaseService.deleteLease(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String extractToken(HttpServletRequest request) {
        // Extract the token from the Authorization header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }
}
