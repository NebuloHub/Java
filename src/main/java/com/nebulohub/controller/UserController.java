package com.nebulohub.controller;


import com.nebulohub.domain.user.CreateUserDto;
import com.nebulohub.domain.user.ReadUserDto;
import com.nebulohub.domain.user.UpdateUserDto;
import com.nebulohub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration and management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user (public)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Username or email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<ReadUserDto> register(
            @RequestBody @Valid CreateUserDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        ReadUserDto newUser = userService.create(dto);
        URI uri = uriBuilder.path("/api/users/{id}").buildAndExpand(newUser.id()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @Operation(summary = "List all users (paginated)")
    @ApiResponse(responseCode = "200", description = "Page of users returned")
    @GetMapping
    public ResponseEntity<Page<ReadUserDto>> listAll(

            @ParameterObject
            @PageableDefault(size = 10, sort = "username") Pageable pageable
    ) {
        Page<ReadUserDto> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get a single user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDto> getById(@PathVariable Long id) {
        ReadUserDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update a user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Username or email already in use")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadUserDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserDto dto
    ) {
        ReadUserDto updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}