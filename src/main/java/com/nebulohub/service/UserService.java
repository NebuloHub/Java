package com.nebulohub.service;

import com.nebulohub.domain.user.*;
import com.nebulohub.exception.AuthenticationException;
import com.nebulohub.exception.DuplicateEntryException;
import com.nebulohub.exception.NotFoundException;
import com.nebulohub.infra.security.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String login(LoginDto dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            Authentication authentication = authenticationManager.authenticate(authToken);

            var user = (User) authentication.getPrincipal();
            return tokenService.generateToken(user);

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Invalid email or password");
        }
    }

    public Page<ReadUserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(ReadUserDto::new);
    }

    @Cacheable(cacheNames = "userProfile", key = "#id")
    public ReadUserDto findById(Long id) {
        return userRepository.findById(id)
                .map(ReadUserDto::new)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Transactional
    public ReadUserDto create(CreateUserDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new DuplicateEntryException("Username already in use: " + dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntryException("Email already in use: " + dto.email());
        }

        String hashedPassword = passwordEncoder.encode(dto.password());

        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setEmail(dto.email());
        newUser.setPassword(hashedPassword);
        newUser.setRole("ROLE_USER");

        User savedUser = userRepository.save(newUser);
        return new ReadUserDto(savedUser);
    }

    @Transactional
    @PreAuthorize("#id == principal.id")
    @CacheEvict(cacheNames = "userProfile", key = "#id")
    public ReadUserDto update(Long id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (dto.username() != null) {
            if (userRepository.existsByUsername(dto.username()) && !user.getActualUsername().equals(dto.username())) {
                throw new DuplicateEntryException("Username already in-use: " + dto.username());
            }
            user.setUsername(dto.username());
        }

        if (dto.email() != null) {
            if (userRepository.existsByEmail(dto.email()) && !user.getEmail().equals(dto.email())) {
                throw new DuplicateEntryException("Email already in-use: " + dto.email());
            }
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.role() != null && authenticatedUser.getRole().equals("ROLE_ADMIN")) {
            user.setRole(dto.role());
        }

        // **A CORREÇÃO:** Salvando o objeto 'user' que acabamos de modificar.
        User updatedUser = userRepository.save(user);
        return new ReadUserDto(updatedUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    @CacheEvict(cacheNames = "userProfile", key = "#id")
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}