package com.sogeti.leaseservice.repository;

import com.sogeti.leaseservice.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {

}
