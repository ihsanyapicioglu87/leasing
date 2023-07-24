package com.allane.leasing.controller;

import com.allane.leasing.model.LeasingContract;
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
    public List<LeasingContract> getAllLeasingContracts() {
        return leasingContractService.getAllLeasingContracts();
    }

    @GetMapping("/{id}")
    public LeasingContract getLeasingContractById(@PathVariable Long id) {
        return leasingContractService.getLeasingContractById(id);
    }

    @PostMapping
    public LeasingContract createLeasingContract(@RequestBody LeasingContract leasingContract) {
        return leasingContractService.createLeasingContract(leasingContract);
    }

    @PutMapping("/update/{id}")
    public LeasingContract updateLeasingContract(@PathVariable Long id, @RequestBody LeasingContract updatedContract) {
        return leasingContractService.updateLeasingContract(id, updatedContract);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLeasingContract(@PathVariable Long id) {
        leasingContractService.deleteLeasingContract(id);
    }
}
