package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.repository.LeasingContractRepository;
import com.allane.leasing.utils.AuditLogUtils;
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
        leasingContractRepository.save(leasingContract);
        AuditLogUtils.logAction(ActionType.CREATE, LeasingContract.class.getSimpleName(), leasingContract.getId(), "ihsan");
        return leasingContract;
    }

    public LeasingContract updateLeasingContract(Long id, LeasingContract updatedContract) {
        LeasingContract existingContract = leasingContractRepository.findById(id).orElse(null);
        if (existingContract != null) {
            existingContract = updatedContract;
            leasingContractRepository.save(existingContract);
            AuditLogUtils.logAction(ActionType.UPDATE, LeasingContract.class.getSimpleName(), id, "ihsan");
            return existingContract;
        }
        return null;
    }

    public void deleteLeasingContract(Long id) {
        leasingContractRepository.deleteById(id);
        AuditLogUtils.logAction(ActionType.DELETE, LeasingContract.class.getSimpleName(), id, "ihsan");
    }
}
