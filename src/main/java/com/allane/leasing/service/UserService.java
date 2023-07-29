package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.model.Customer;
import com.allane.leasing.model.LeasingContract;
import com.allane.leasing.model.User;
import com.allane.leasing.repository.UserRepository;
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
public class UserService {
    private final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Retrieved users from database. Size of the list: " + users.size());
        }
        return users;
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        LOG.info("User retrieved with the id: " + id + ". Username: " + user.getUsername());
        return user;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        LOG.info("User retrieved with the username: " + user.getUsername());
        return user;
    }

    public User createUser(User user) {
        try {
            User createdUser = userRepository.save(user);
            AuditLogUtils.logAction(ActionType.CREATE, User.class.getSimpleName(), user.getId(), "ihsan");
            LOG.info("New user has been created");
            return createdUser;
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while saving user: " + ex.getMessage(), ex);
        }
    }

    public User updateUser(User updatedUser) {
        Long id = updatedUser.getId();
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }

        try {
            userRepository.save(updatedUser);
            AuditLogUtils.logAction(ActionType.UPDATE, User.class.getSimpleName(), id, "ihsan");
            LOG.info("User has been updated with the id: " + id);
            return updatedUser;
        } catch (TransactionSystemException | JpaSystemException ex) {
            throw new RuntimeException("Error with transaction or JPA system: " + ex.getMessage(), ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Error updating user due to optimistic lock: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while updating user: " + ex.getMessage(), ex);
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            AuditLogUtils.logAction(ActionType.DELETE, User.class.getSimpleName(), id, "ihsan");
            LOG.info("User has been deleted with the id: " + id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting user: " + ex.getMessage(), ex);
        }
    }
}
