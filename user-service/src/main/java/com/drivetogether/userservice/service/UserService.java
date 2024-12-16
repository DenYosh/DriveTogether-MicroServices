package com.drivetogether.userservice.service;

import com.drivetogether.userservice.dto.UserRequestDTO;
import com.drivetogether.userservice.dto.UserResponseDTO;
import com.drivetogether.userservice.model.User;
import com.drivetogether.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = User.builder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .address(userRequestDTO.getAddress())
                .build();
        user = userRepository.save(user);
        return mapToResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}
