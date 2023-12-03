package com.sogeti.leaseservice.client;

import com.sogeti.leaseservice.dto.CarDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "car-service", url = "${car.service.url}") // Specify the Feign client name and the target service URL
@Qualifier("carFeignClient")
public interface CarFeignClient {
    @GetMapping("/api/cars/{id}")
    ResponseEntity<CarDTO> getCarById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}
