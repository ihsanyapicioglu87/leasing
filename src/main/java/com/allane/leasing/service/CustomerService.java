package com.allane.leasing.service;

import com.allane.leasing.controller.CustomerController;
import com.allane.leasing.dto.CustomerDTO;
import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.model.Customer;
import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.repository.AuditLogRepository;
import com.allane.leasing.repository.CustomerRepository;
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
public class CustomerService {
    private final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    private final MessageSource messageSource;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, MessageSource messageSource) {
        this.customerRepository = customerRepository;
        this.messageSource = messageSource;
    }

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved customers from database. Size of the list: " + customers.size());
        }
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        LOG.info("Customer retrieved with the id: " + id + ". Customer info: " + customer.getFirstName() + " " +customer.getLastName());
        return convertToDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = convertToEntity(customerDTO);
            customer = customerRepository.save(customer);
            if (customer == null) {
                throw new RuntimeException("Error while saving customer: The saved customer object is null.");
            }

            CustomerDTO newCustomerDTO = convertToDTO(customer);
            AuditLogUtils.logAction(ActionType.CREATE, Customer.class.getSimpleName(), customer.getId(), "ihsan");
            LOG.info("New customer has been created");
            return newCustomerDTO;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while creating customer: " + ex.getMessage(), ex);
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
            CustomerDTO newUpdatedCustomerDTO = convertToDTO(updatedCustomer);
            AuditLogUtils.logAction(ActionType.UPDATE, Customer.class.getSimpleName(), id, "ihsan");
            LOG.info("Customer has been updated with the id: " + id);
            return newUpdatedCustomerDTO;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating customer: " + ex.getMessage(), ex);
        }
    }

    public void deleteCustomer(Long id) {
        try {
            customerRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, Customer.class.getSimpleName(), id, "ihsan");
            LOG.info("Customer has been deleted with the id: " + id);
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
