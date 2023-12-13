package com.evaluacion.dSegovia.controller;

import com.evaluacion.dSegovia.dto.UserDTO;
import com.evaluacion.dSegovia.model.UserCredentials;
import com.evaluacion.dSegovia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    //Logiin
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials credentials) {
        try {
            String token = userService.login(credentials.getEmail(), credentials.getPassword());
            return ResponseEntity.ok(Collections.singletonMap("mensaje", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> getAllUsersPaginated(@RequestParam int page, @RequestParam int size) {
        Page<UserDTO> usersPage = userService.getAllUsers(page, size);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String email, @PathVariable UUID id) {
        userService.deleteUser(email, id);
        return ResponseEntity.ok().build();
    }
}
