package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.model.Customer;
import com.allane.leasing.repository.AuditLogRepository;
import com.allane.leasing.repository.CustomerRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer createCustomer(Customer customer) {
        customerRepository.save(customer);
        AuditLogUtils.logAction(ActionType.CREATE, Customer.class.getSimpleName(), customer.getId(), "ihsan");
        return customer;
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            existingCustomer = updatedCustomer;
            AuditLogUtils.logAction(ActionType.UPDATE, Customer.class.getName(), existingCustomer.getId(), "ihsan");
            customerRepository.save(existingCustomer);
            return existingCustomer;
        }
        return null;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        AuditLogUtils.logAction(ActionType.DELETE, Customer.class.getName(), id, "ihsan");
    }
}
