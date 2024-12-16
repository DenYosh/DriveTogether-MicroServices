package com.drivetogether.userservice.controller;

import com.drivetogether.userservice.dto.UserDTO;
import com.drivetogether.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userdto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userdto));
    }
}
