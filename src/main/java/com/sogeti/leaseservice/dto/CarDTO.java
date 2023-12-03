package com.sogeti.leaseservice.dto;

import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    private String make;
    private String model;
    private String version;
    private int numberOfDoors;
    private double co2Emission;
    private double grossPrice;
    private double nettPrice;
}
