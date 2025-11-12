package com.nebulohub.service;

import com.nebulohub.domain.user.*;

import com.nebulohub.exception.DuplicateEntryException;
import com.nebulohub.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Lombok constructor injection
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injected from PasswordEncoderConfig

    /**
     * Finds all users with pagination.
     */
    public Page<ReadUserDto> findAll(Pageable pageable) {
        // Map Page<User> to Page<ReadUserDto> using the correct constructor
        return userRepository.findAll(pageable).map(ReadUserDto::new);
    }

    /**
     * Finds a single user by their ID.
     */
    public ReadUserDto findById(Long id) {
        return userRepository.findById(id)
                .map(ReadUserDto::new)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    /**
     * Creates a new user.
     */
    @Transactional
    public ReadUserDto create(CreateUserDto dto) {
        // 1. Check for duplicates
        if (userRepository.existsByUsername(dto.username())) {
            throw new DuplicateEntryException("Username already in use: " + dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntryException("Email already in use: " + dto.email());
        }

        // 2. Hash the password
        String hashedPassword = passwordEncoder.encode(dto.password());

        // 3. Create and save the new user
        User newUser = new User();
        newUser.setUsername(dto.username()); // Sets the actual username
        newUser.setEmail(dto.email());       // Sets the login email
        newUser.setPassword(hashedPassword);
        newUser.setRole("ROLE_USER"); // Default role

        User savedUser = userRepository.save(newUser);
        return new ReadUserDto(savedUser);
    }

    /**
     * Updates an existing user.
     */
    @Transactional
    public ReadUserDto update(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // Perform partial updates
        if (dto.username() != null) {
            // Check if new username is already taken by *another* user
            if (userRepository.existsByUsername(dto.username()) && !user.getActualUsername().equals(dto.username())) {
                throw new DuplicateEntryException("Username already in use: " + dto.username());
            }
            user.setUsername(dto.username());
        }

        if (dto.email() != null) {
            // Check if new email is already taken by *another* user
            if (userRepository.existsByEmail(dto.email()) && !user.getEmail().equals(dto.email())) {
                throw new DuplicateEntryException("Email already in use: " + dto.email());
            }
            user.setEmail(dto.email());
        }

        if (dto.password() != null) {
            // Re-hash the password if it's being changed
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.role() != null) {
            // Optionally allow role update
            user.setRole(dto.role());
        }

        User updatedUser = userRepository.save(user);
        return new ReadUserDto(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     */
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}