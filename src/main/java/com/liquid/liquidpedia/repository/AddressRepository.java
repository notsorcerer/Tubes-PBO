package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerId(Long customerId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.customer.id = :customerId")
    void resetDefaultByCustomerId(@Param("customerId") Long customerId);
}
