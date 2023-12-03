package com.sogeti.leaseservice.config;

import com.sogeti.leaseservice.utility.LeaseMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public LeaseMapper leaseMapper() {
        return Mappers.getMapper(LeaseMapper.class);
    }
}
