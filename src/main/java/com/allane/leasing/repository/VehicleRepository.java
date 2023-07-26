package com.allane.leasing.repository;

import com.allane.leasing.dto.VehicleDTO;
import com.allane.leasing.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
