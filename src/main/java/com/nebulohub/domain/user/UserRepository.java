package com.nebulohub.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email. Used by Spring Security's UserDetailsService.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their public username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if an email is already in use.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a username is already in use.
     */
    boolean existsByUsername(String username);
}