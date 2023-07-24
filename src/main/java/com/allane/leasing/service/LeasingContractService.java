package com.allane.leasing.service;

import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.repository.LeasingContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeasingContractService {

    private final LeasingContractRepository leasingContractRepository;

    @Autowired
    public LeasingContractService(LeasingContractRepository leasingContractRepository) {
        this.leasingContractRepository = leasingContractRepository;
    }

    public List<LeasingContract> getAllLeasingContracts() {
        return leasingContractRepository.findAll();
    }

    public LeasingContract getLeasingContractById(Long id) {
        return leasingContractRepository.findById(id).orElse(null);
    }

    public LeasingContract createLeasingContract(LeasingContract leasingContract) {
        return leasingContractRepository.save(leasingContract);
    }

    public LeasingContract updateLeasingContract(Long id, LeasingContract updatedContract) {
        LeasingContract existingContract = leasingContractRepository.findById(id).orElse(null);
        if (existingContract != null) {
            existingContract = updatedContract;

            return leasingContractRepository.save(existingContract);
        }
        return null; // Or throw an exception indicating the contract was not found
    }

    public void deleteLeasingContract(Long id) {
        leasingContractRepository.deleteById(id);
    }
}
