package com.drivetogether.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
