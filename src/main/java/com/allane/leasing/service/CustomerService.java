package com.allane.leasing.service;

import com.allane.leasing.dto.CustomerDTO;
import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.model.Customer;
import com.allane.leasing.repository.AuditLogRepository;
import com.allane.leasing.repository.CustomerRepository;
import com.allane.leasing.utils.AuditLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return convertToDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = convertToEntity(customerDTO);
            customer = customerRepository.save(customer);
            return convertToDTO(customer);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while saving customer: " + ex.getMessage(), ex);
        }
    }

    public CustomerDTO updateCustomer(CustomerDTO updatedCustomerDTO) {
        Long id = updatedCustomerDTO.getId();
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer with id " + id + " not found.");
        }

        try {
            Customer updatedCustomer = convertToEntity(updatedCustomerDTO);
            customerRepository.save(updatedCustomer);
            return convertToDTO(updatedCustomer);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating customer: " + ex.getMessage(), ex);
        }
    }

    public void deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, CustomerDTO.class.getSimpleName(), id, "ihsan");
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("CustomerDTO with id " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting customer: " + ex.getMessage(), ex);
        }
    }

    public CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setBirthdate(customer.getBirthdate());
        return customerDTO;
    }
    public Customer convertToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setBirthdate(customerDTO.getBirthdate());
        return customer;
    }
}
