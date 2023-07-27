package com.allane.leasing.repository;

import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeasingContractRepository extends JpaRepository<LeasingContract, Long> {

}
