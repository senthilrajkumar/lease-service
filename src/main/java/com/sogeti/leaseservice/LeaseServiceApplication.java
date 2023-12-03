package com.sogeti.leaseservice;

import com.sogeti.leaseservice.config.MapperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MapperConfig.class)
@EnableFeignClients
public class LeaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaseServiceApplication.class, args);
	}

}
