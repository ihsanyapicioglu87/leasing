package com.allane.leasing.service;

import com.allane.leasing.dto.LeasingContractDTO;
import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.model.Customer;
import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.model.Vehicle;
import com.allane.leasing.repository.CustomerRepository;
import com.allane.leasing.repository.LeasingContractRepository;
import com.allane.leasing.repository.VehicleRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeasingContractService {
    private final Logger LOG = LoggerFactory.getLogger(LeasingContractService.class);
    private final LeasingContractRepository leasingContractRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MessageSource messageSource;


    @Autowired
    public LeasingContractService(LeasingContractRepository leasingContractRepository, CustomerRepository customerRepository,
                                  VehicleRepository vehicleRepository, MessageSource messageSource) {
        this.leasingContractRepository = leasingContractRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.messageSource = messageSource;
    }

    public List<LeasingContractDTO> getAllLeasingContracts() {
        List<LeasingContract> leasingContracts = leasingContractRepository.findAll();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved leasing contracts from database. Size of the list: " + leasingContracts.size());
        }
        return leasingContracts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LeasingContractDTO getLeasingContractById(Long id) {
        LeasingContract leasingContract = leasingContractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leasing contract not found with ID: " + id));
        LOG.info("Leasing contract retrieved with the id: " + id + ". Contract number: " + leasingContract.getContractNo());
        return convertToDTO(leasingContract);
    }

    public LeasingContractDTO createLeasingContract(LeasingContractDTO leasingContractDTO) {
        try {
            LeasingContract leasingContract = convertToEntity(leasingContractDTO);
            leasingContract = leasingContractRepository.save(leasingContract);
            LeasingContractDTO newLeasingContractDTO = convertToDTO(leasingContract);

            AuditLogUtils.logAction(ActionType.CREATE, LeasingContract.class.getSimpleName(), leasingContract.getId(), "ihsan");
            LOG.info("New leasing contract has been created");
            return newLeasingContractDTO;
        } catch (DataAccessException ex) {
            throw new RuntimeException(messageSource.getMessage("exception.access",
                    null, Locale.getDefault()) + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException(messageSource.getMessage("leasingContract.save.exception",
                    null, Locale.getDefault()) + ex.getMessage(), ex);
        }
    }

    public LeasingContractDTO updateLeasingContract(LeasingContractDTO updatedLeasingContractDTO) {
        Long id = updatedLeasingContractDTO.getId();
        Optional<LeasingContract> existingLeasingContract = leasingContractRepository.findById(id);

        if (existingLeasingContract.isEmpty()) {
            throw new ResourceNotFoundException("Leasing contract with id " + id + " not found.");
        }

        try {
            LeasingContract updatedContract = convertToEntity(updatedLeasingContractDTO);
            leasingContractRepository.save(updatedContract);
            LeasingContractDTO newUpdatedLeasingContractDTO = convertToDTO(updatedContract);
            AuditLogUtils.logAction(ActionType.UPDATE, LeasingContract.class.getSimpleName(), id, "ihsan");
            LOG.info("Leasing contract has been updated with the id: " + id);
            return newUpdatedLeasingContractDTO;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating leasing contract: " + ex.getMessage(), ex);
        }
    }

    public void deleteLeasingContract(Long id) {
        try {
            leasingContractRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, LeasingContract.class.getSimpleName(), id, "ihsan");
            LOG.info("Leasing contract has been deleted with the id: " + id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Leasing contract with id " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting leasing contract: " + ex.getMessage(), ex);
        }
    }

    public LeasingContractDTO convertToDTO(LeasingContract leasingContract) {
        LeasingContractDTO leasingContractDTO = new LeasingContractDTO();
        leasingContractDTO.setId(leasingContract.getId());
        leasingContractDTO.setContractNo(leasingContract.getContractNo());
        leasingContractDTO.setMonthlyRate(leasingContract.getMonthlyRate());
        leasingContractDTO.setCustomerId(leasingContract.getCustomer().getId());
        leasingContractDTO.setVehicleId(leasingContract.getVehicle().getId());
        return leasingContractDTO;
    }

    public LeasingContract convertToEntity(LeasingContractDTO leasingContractDTO) {
        LeasingContract leasingContract = new LeasingContract();
        leasingContract.setId(leasingContractDTO.getId());
        leasingContract.setContractNo(leasingContractDTO.getContractNo());
        leasingContract.setMonthlyRate(leasingContractDTO.getMonthlyRate());

        Long customerId = leasingContractDTO.getCustomerId();
        Long vehicleId = leasingContractDTO.getVehicleId();
        Customer customer = customerRepository.findById(leasingContractDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        Vehicle vehicle = vehicleRepository.findById(leasingContractDTO.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));

        leasingContract.setCustomer(customer);
        leasingContract.setVehicle(vehicle);

        return leasingContract;
    }
}
