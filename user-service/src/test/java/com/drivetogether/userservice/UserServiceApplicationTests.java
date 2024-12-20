package com.drivetogether.userservice;

import com.drivetogether.userservice.dto.UserCarsResponse;
import com.drivetogether.userservice.dto.UserRequestDTO;
import com.drivetogether.userservice.dto.UserResponseDTO;
import com.drivetogether.userservice.dto.VehicleDTO;
import com.drivetogether.userservice.model.User;
import com.drivetogether.userservice.repository.UserRepository;
import com.drivetogether.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceApplicationTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @Test
    public void testCreateUser() {
        // Arrange
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPhoneNumber("1234567890");
        userRequestDTO.setAddress("123 Main St");

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        // Assert
        assertEquals("John Doe", userResponseDTO.getName());
        assertEquals("john.doe@example.com", userResponseDTO.getEmail());
        assertEquals("1234567890", userResponseDTO.getPhoneNumber());
        assertEquals("123 Main St", userResponseDTO.getAddress());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1L);
        VehicleDTO[] vehicleDTOArray = {vehicleDTO};

        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(eq("http://null/api/vehicle/user/" + userId))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(VehicleDTO[].class)).thenReturn(Mono.just(vehicleDTOArray));

        // Act
        UserCarsResponse userCarsResponse = userService.getUserById(userId);

        // Assert
        assertEquals(userId, userCarsResponse.getId());
        assertEquals("John Doe", userCarsResponse.getName());
        assertEquals("john.doe@example.com", userCarsResponse.getEmail());
        assertEquals("1234567890", userCarsResponse.getPhoneNumber());
        assertEquals("123 Main St", userCarsResponse.getAddress());
        assertEquals(1, userCarsResponse.getVehicles().size());
        assertEquals(1L, userCarsResponse.getVehicles().get(0).getId());

        verify(userRepository, times(1)).findById(eq(userId));
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpecMock, times(1)).uri(eq("http://null/api/vehicle/user/" + userId));
        verify(requestHeadersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(VehicleDTO[].class);
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setAddress("123 Main St");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // Act
        List<UserResponseDTO> userResponseDTOs = userService.getAllUsers();

        // Assert
        assertEquals(1, userResponseDTOs.size());
        assertEquals("John Doe", userResponseDTOs.get(0).getName());
        assertEquals("john.doe@example.com", userResponseDTOs.get(0).getEmail());
        assertEquals("1234567890", userResponseDTOs.get(0).getPhoneNumber());
        assertEquals("123 Main St", userResponseDTOs.get(0).getAddress());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserByIdWhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(eq(userId))).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.getUserById(userId);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findById(eq(userId));
    }
}
