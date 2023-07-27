package com.allane.leasing.repository;

import com.allane.leasing.dto.VehicleDTO;
import com.allane.leasing.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByLeasingContractIsNull();
}
