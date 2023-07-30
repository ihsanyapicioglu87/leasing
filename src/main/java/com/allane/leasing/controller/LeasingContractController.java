package com.allane.leasing.controller;

import com.allane.leasing.dto.LeasingContractDTO;
import com.allane.leasing.dto.VehicleDTO;
import com.allane.leasing.model.Vehicle;
import com.allane.leasing.service.LeasingContractService;
import com.allane.leasing.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/leasing-contracts")
public class LeasingContractController {

    private final LeasingContractService leasingContractService;

    private final VehicleService vehicleService;
    @Autowired
    public LeasingContractController(LeasingContractService leasingContractService, VehicleService vehicleService) {
        this.leasingContractService = leasingContractService;
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<LeasingContractDTO> getAllLeasingContracts() {
        return leasingContractService.getAllLeasingContracts();
    }

    @GetMapping("/{id}")
    public LeasingContractDTO getLeasingContractById(@PathVariable Long id) {
        return leasingContractService.getLeasingContractById(id);
    }

    @GetMapping("/availableVehicles")
    public List<VehicleDTO> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    @PostMapping
    public LeasingContractDTO createLeasingContract(@RequestBody LeasingContractDTO leasingContractDTO) {
        return leasingContractService.createLeasingContract(leasingContractDTO);
    }

    @PutMapping("/update")
    public LeasingContractDTO updateLeasingContract(@RequestBody LeasingContractDTO leasingContractDTO) {
        return leasingContractService.updateLeasingContract(leasingContractDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLeasingContract(@PathVariable Long id) {
        leasingContractService.deleteLeasingContract(id);
    }
}
