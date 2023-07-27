package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.exception.RoleNotFoundException;
import com.allane.leasing.model.Role;
import com.allane.leasing.repository.RoleRepository;
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

@Service
public class RoleService {
    private final Logger LOG = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved roles from database. Size of the list: " + roles.size());
        }

        return roles;
    }

    public Role getRoleById(Long id) throws RoleNotFoundException {
         Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + id));
        LOG.info("Role retrieved with the id: " + id + ". Role name: " + role.getName());
        return role;
    }

    public Role createRole(Role role) {
        try {
            Role createdRole = roleRepository.save(role);
            AuditLogUtils.logAction(ActionType.CREATE, Role.class.getSimpleName(), role.getId(), "ihsan");
            LOG.info("New Role has been created");
            return createdRole;
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while saving the role: " + ex.getMessage(), ex);
        }
    }

    public Role updateRole(Role updatedRole) throws RoleNotFoundException {
        Long id = updatedRole.getId();
        Optional<Role> existingRole = roleRepository.findById(id);

        if (existingRole.isEmpty()) {
            throw new RoleNotFoundException("Role not found with ID: " + updatedRole.getId());
        }

        try {
            roleRepository.save(updatedRole);
            AuditLogUtils.logAction(ActionType.UPDATE, Role.class.getSimpleName(), id, "ihsan");
            LOG.info("Role has been updated with the id: " + id);
            return updatedRole;
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Error updating the role due to optimistic lock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating the role: " + ex.getMessage(), ex);
        }
    }

    public void deleteRole(Long id) throws RoleNotFoundException {
        try {
            roleRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, Role.class.getSimpleName(), id, "ihsan");
            LOG.info("Role has been deleted with the id: " + id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Role with id " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting the role: " + ex.getMessage(), ex);
        }
    }
}
