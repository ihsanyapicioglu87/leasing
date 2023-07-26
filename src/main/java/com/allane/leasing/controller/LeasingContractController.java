package com.allane.leasing.controller;

import com.allane.leasing.dto.LeasingContractDTO;
import com.allane.leasing.service.LeasingContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leasing-contracts")
public class LeasingContractController {

    private final LeasingContractService leasingContractService;

    @Autowired
    public LeasingContractController(LeasingContractService leasingContractService) {
        this.leasingContractService = leasingContractService;
    }

    @GetMapping
    public List<LeasingContractDTO> getAllLeasingContracts() {
        return leasingContractService.getAllLeasingContracts();
    }

    @GetMapping("/{id}")
    public LeasingContractDTO getLeasingContractById(@PathVariable Long id) {
        return leasingContractService.getLeasingContractById(id);
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
