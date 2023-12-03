package com.sogeti.leaseservice.service;

import com.sogeti.leaseservice.client.CarFeignClient;
import com.sogeti.leaseservice.client.CustomerFeignClient;
import com.sogeti.leaseservice.client.IAMFeignClient;
import com.sogeti.leaseservice.dto.CarDTO;
import com.sogeti.leaseservice.dto.CustomerDTO;
import com.sogeti.leaseservice.dto.LeaseDTO;
import com.sogeti.leaseservice.exception.ApplicationException;
import com.sogeti.leaseservice.exception.CustomerNotFoundException;
import com.sogeti.leaseservice.exception.TokenValidationException;
import com.sogeti.leaseservice.model.Lease;
import com.sogeti.leaseservice.repository.LeaseRepository;
import com.sogeti.leaseservice.utility.LeaseMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LeaseServiceImpl implements  LeaseService {

    public static final String BEARER = "Bearer ";
    public static final String USER_IS_NOT_AUTHENTICATED = "User is not authenticated";
    public static final String TOKEN_VALIDATION_FAILED = "Token validation failed";
    private final LeaseRepository leaseRepository;
    @Qualifier("iamFeignClient")
    private final IAMFeignClient iamFeignClient;
    @Qualifier("customerFeignClient")
    private final CustomerFeignClient customerFeignClient;
    @Qualifier("carFeignClient")
    private final CarFeignClient carFeignClient;
    private final LeaseMapper leaseMapper;

    @Override
    public LeaseDTO createLease(LeaseDTO leaseDTO) {
        Lease lease = leaseMapper.leaseDTOToLease(leaseDTO);
        // Add business logic for lease rate calculation
        calculateLeaseRate(lease);
        return leaseMapper.leaseToLeaseDTO(leaseRepository.save(lease));
    }

    @Override
    public void validateCustomer(Long customerId, String token) {
        // Add logic to handle the response or throw an exception if validation fails
        try {
            String authorizationHeader = BEARER + token;
            // Call the token-validation endpoint of customer-service using Feign Client
            ResponseEntity<CustomerDTO> response = customerFeignClient.getCustomerById(customerId, authorizationHeader);
            if(!response.hasBody()){
                throw new CustomerNotFoundException(customerId + " does not exist");
            }
        } catch(FeignException e) {
            if(e.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error(USER_IS_NOT_AUTHENTICATED);
                throw new TokenValidationException(TOKEN_VALIDATION_FAILED);
            } else {
                log.error("Internal server error occurred whn fetching customer details");
                throw new ApplicationException("Internal Server Error");
            }
        }
    }

    @Override
    public void validateCar(Long carId, String token) {
        // Add logic to handle the response or throw an exception if validation fails
        try {
            String authorizationHeader = BEARER + token;
            // Call the token-validation endpoint of car-service using Feign Client
            ResponseEntity<CarDTO> response = carFeignClient.getCarById(carId, authorizationHeader);
            if(!response.hasBody()){
                throw new CustomerNotFoundException(carId + " does not exist");
            }
        } catch(FeignException e) {
            if(e.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error(USER_IS_NOT_AUTHENTICATED);
                throw new TokenValidationException(TOKEN_VALIDATION_FAILED);
            } else {
                log.error("Internal server error occurred whn fetching car details");
                throw new ApplicationException("Internal Server Error");
            }
        }
    }

    @Override
    public void deleteLease(Long id) {
        leaseRepository.deleteById(id);
    }

    @Override
    public LeaseDTO updateLease(Long id, LeaseDTO leaseDTO) {
      Lease existingLease = leaseRepository.findById(id).orElse(null);
        if (existingLease != null) {
            Lease updatedLease = leaseRepository.save(leaseMapper.leaseDTOToLease(leaseDTO));
            return leaseMapper.leaseToLeaseDTO(updatedLease);
        }
        return null;
    }

    @Override
    public LeaseDTO getLeaseById(Long id) {
        Lease lease = leaseRepository.findById(id).orElse(null);
        return (lease != null) ? leaseMapper.leaseToLeaseDTO(lease) : null;
    }

    @Override
    public List<LeaseDTO> getAllLeases() {
      List<Lease> leases = leaseRepository.findAll();
        return leases.stream()
                .map(leaseMapper::leaseToLeaseDTO)
                .toList();
    }

    private void calculateLeaseRate(Lease lease) {
        // Add business logic for lease rate calculation
        double leaseRate = (((double) lease.getMileage() / 12) * lease.getDuration()) / lease.getNettPrice()
                + (((lease.getInterestRate() / 100) * lease.getNettPrice()) / 12);

        lease.setLeaseRate(leaseRate);
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            String authorizationHeader = BEARER + token;
            // Call the token-validation endpoint of iam-service using Feign Client
            ResponseEntity<String> response = iamFeignClient.validateToken(authorizationHeader);
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error(USER_IS_NOT_AUTHENTICATED);
                throw new TokenValidationException(TOKEN_VALIDATION_FAILED);
            }
        } catch(FeignException e) {
            log.error(USER_IS_NOT_AUTHENTICATED);
            throw new TokenValidationException(TOKEN_VALIDATION_FAILED);
        }
        log.info("User is successfully authenticated");
        return true;
    }
}
