package com.sogeti.leaseservice.dto;

import lombok.Data;

@Data
public class LeaseDTO {

    private Long customerId;
    private Long carId;
    private int mileage;
    private int duration;
    private double interestRate;
    private double nettPrice;
}
