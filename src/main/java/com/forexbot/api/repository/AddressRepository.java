package com.forexbot.api.repository;

import com.forexbot.api.dao.admin.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address save(Address address);
}