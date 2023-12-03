package com.sogeti.leaseservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Lease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long carId;
    private int mileage;
    private int duration;
    private double interestRate;
    private double nettPrice;
    private double leaseRate; // a field to store lease rate

}
