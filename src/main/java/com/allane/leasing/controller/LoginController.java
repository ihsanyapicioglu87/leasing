package com.allane.leasing.controller;

import com.allane.leasing.model.Role;
import com.allane.leasing.model.User;
import com.allane.leasing.utils.LoginRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        JSONObject body = new JSONObject();
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                User user = new User();
                user.setUsername(userDetails.getUsername());
                Set<Role> roles = convertAuthoritiesToRoles(userDetails.getAuthorities());
                user.setRoles(roles);
                body.put("user", user);
                return new ResponseEntity<>(body.toMap(), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
    }

    private Set<Role> convertAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        Set<Role> roles = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            Role role = new Role();
            role.setName(authority.getAuthority());
            roles.add(role);
        }
        return roles;
    }
}
