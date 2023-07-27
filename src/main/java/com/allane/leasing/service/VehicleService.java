package com.allane.leasing.service;

import com.allane.leasing.dto.CustomerDTO;
import com.allane.leasing.dto.VehicleDTO;
import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.exception.VehicleNotFoundException;
import com.allane.leasing.model.Customer;
import com.allane.leasing.model.Vehicle;
import com.allane.leasing.repository.VehicleRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved vehicles from database. Size of the list: " + vehicles.size());
        }
        return vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAllByLeasingContractIsNull();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved vehicles from database. Size of the list: " + vehicles.size());
        }
        return vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        LOG.info("Vehicle retrieved with the id: " + id + ". Vehicle Info: " + vehicle.getBrand() + "-" + vehicle.getModel());
        return convertToDTO(vehicle);
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        try {
            Vehicle vehicle = convertToEntity(vehicleDTO);
            vehicle = vehicleRepository.save(vehicle);
            VehicleDTO newVehicleDTO = convertToDTO(vehicle);

            AuditLogUtils.logAction(ActionType.CREATE, Vehicle.class.getSimpleName(), vehicle.getId(), "ihsan");
            LOG.info("New vehicle has been created");
            return newVehicleDTO;
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
            VehicleDTO newUpdatedVehicleDTO = convertToDTO(updatedVehicle);
            AuditLogUtils.logAction(ActionType.UPDATE, Vehicle.class.getSimpleName(), id, "ihsan");
            LOG.info("Vehicle has been updated with the id: " + id);
            return newUpdatedVehicleDTO;
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
            AuditLogUtils.logAction(ActionType.DELETE, Vehicle.class.getSimpleName(), id, "ihsan");
            LOG.info("Vehicle has been deleted with the id: " + id);
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
