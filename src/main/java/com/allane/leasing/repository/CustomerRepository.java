package com.allane.leasing.repository;

import com.allane.leasing.dto.CustomerDTO;
import com.allane.leasing.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
