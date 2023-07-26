package com.allane.leasing.service;

import com.allane.leasing.dto.VehicleDTO;
import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.exception.VehicleNotFoundException;
import com.allane.leasing.model.Vehicle;
import com.allane.leasing.repository.VehicleRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        return convertToDTO(vehicle);
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        try {
            Vehicle vehicle = convertToEntity(vehicleDTO);
            vehicle = vehicleRepository.save(vehicle);
            return convertToDTO(vehicle);
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while saving vehicleDTO: " + ex.getMessage(), ex);
        }
    }

    public VehicleDTO updateVehicle(VehicleDTO updatedVehicleDTO) {
        Long id = updatedVehicleDTO.getId();
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);

        if (existingVehicle.isEmpty()) {
            throw new ResourceNotFoundException("Vehicle with id " + id + " not found.");
        }

        try {
            Vehicle updatedVehicle = convertToEntity(updatedVehicleDTO);
            vehicleRepository.save(updatedVehicle);
            return convertToDTO(updatedVehicle);
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Error updating vehicleDTO due to optimistic lock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating vehicleDTO: " + ex.getMessage(), ex);
        }
    }


    public void deleteVehicle(Long id) {
        try {
            vehicleRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, VehicleDTO.class.getSimpleName(), id, "ihsan");
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("VehicleDTO with id " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting vehicleDTO: " + ex.getMessage(), ex);
        }
    }

    public VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setBrand(vehicle.getBrand());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setModelYear(vehicle.getModelYear());
        vehicleDTO.setVin(vehicle.getVin());
        vehicleDTO.setPrice(vehicle.getPrice());
        return vehicleDTO;
    }

    public Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleDTO.getId());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setModelYear(vehicleDTO.getModelYear());
        vehicle.setVin(vehicleDTO.getVin());
        vehicle.setPrice(vehicleDTO.getPrice());
        return vehicle;
    }
}
