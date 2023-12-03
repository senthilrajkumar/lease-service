package com.sogeti.leaseservice.service;

import com.sogeti.leaseservice.dto.LeaseDTO;

import java.util.List;

public interface LeaseService {
    LeaseDTO createLease(LeaseDTO leaseDTO);

    boolean isValidToken(String token);

    void validateCustomer(Long customerId, String token);

    void validateCar(Long carId, String token);

    void deleteLease(Long id);

    LeaseDTO updateLease(Long id, LeaseDTO leaseDTO);

    LeaseDTO getLeaseById(Long id);

    List<LeaseDTO> getAllLeases();
}
