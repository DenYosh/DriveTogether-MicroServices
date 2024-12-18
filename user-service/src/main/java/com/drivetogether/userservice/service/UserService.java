package com.drivetogether.userservice.service;

import com.drivetogether.userservice.dto.UserCarsResponse;
import com.drivetogether.userservice.dto.UserRequestDTO;
import com.drivetogether.userservice.dto.UserResponseDTO;
import com.drivetogether.userservice.dto.VehicleDTO;
import com.drivetogether.userservice.model.User;
import com.drivetogether.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${vehicleservice.baseurl}")
    private String vehicleServiceBaseUrl;

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

    public UserResponseDTO editUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.getReferenceById(id);
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setAddress(userRequestDTO.getAddress());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());

        return mapToResponseDTO(userRepository.save(user));
    }

    public UserCarsResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToCarsResponseDTO(user);
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

    private UserCarsResponse mapToCarsResponseDTO(User user) {
        VehicleDTO[] vehicleDTOS = webClient.get()
                .uri("http://" + vehicleServiceBaseUrl + "/api/vehicle/user/" + user.getId())
                .retrieve()
                .bodyToMono(VehicleDTO[].class)
                .block();

        if (vehicleDTOS != null) {
            return UserCarsResponse.builder()
                    .id(user.getId())
                    .vehicles(List.of(vehicleDTOS))
                    .name(user.getName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .address(user.getAddress())
                    .build();
        }

        return UserCarsResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}
