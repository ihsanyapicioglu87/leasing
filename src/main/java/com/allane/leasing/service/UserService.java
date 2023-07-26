package com.allane.leasing.service;

import com.allane.leasing.enums.ActionType;
import com.allane.leasing.exception.ResourceNotFoundException;
import com.allane.leasing.model.User;
import com.allane.leasing.repository.UserRepository;
import com.allane.leasing.utils.AuditLogUtils;
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

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        try {
            User createdUser = userRepository.save(user);
            AuditLogUtils.logAction(ActionType.CREATE, User.class.getSimpleName(), user.getId(), "ihsan");
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
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error accessing data: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unknown error occurred while deleting user: " + ex.getMessage(), ex);
        }
    }
}
