package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.model.Vehicle;
import com.allane.leasing.repository.VehicleRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        AuditLogUtils.logAction(ActionType.CREATE, Vehicle.class.getSimpleName(), vehicle.getId(), "ihsan");
        return vehicle;
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id).orElse(null);
        if (existingVehicle != null) {
            existingVehicle = updatedVehicle;
            vehicleRepository.save(existingVehicle);
            AuditLogUtils.logAction(ActionType.UPDATE, Vehicle.class.getSimpleName(), id, "ihsan");
            return existingVehicle;
        }
        return null;
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
        AuditLogUtils.logAction(ActionType.DELETE, Vehicle.class.getSimpleName(), id, "ihsan");
    }
}
