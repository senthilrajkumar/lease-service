package com.sogeti.leaseservice.utility;

import com.sogeti.leaseservice.dto.LeaseDTO;
import com.sogeti.leaseservice.model.Lease;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaseMapper {
    LeaseDTO leaseToLeaseDTO(Lease lease);
    Lease leaseDTOToLease(LeaseDTO leaseDTO);
}
