package com.drivetogether.vehicleservice;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.drivetogether.vehicleservice.dto.*;
import com.drivetogether.vehicleservice.model.Vehicle;
import com.drivetogether.vehicleservice.model.VehicleModel;
import com.drivetogether.vehicleservice.repository.VehicleRepository;
import com.drivetogether.vehicleservice.repository.VehicleModelRepository;
import com.drivetogether.vehicleservice.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceUnitTests {
    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(vehicleService, "userServiceBaseUrl", "http://localhost:8080");
    }

    @Test
    public void testCreateVehicle() {
        // Arrange
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setId(1L);
        vehicleModel.setModelName("TestModel");

        VehicleOwnerDTO vehicleOwnerDTO = new VehicleOwnerDTO();
        vehicleOwnerDTO.setId(1L);

        VehicleRequestDTO vehicleRequestDTO = new VehicleRequestDTO();
        vehicleRequestDTO.setOwnerId(1L);
        vehicleRequestDTO.setLicensePlate("ABC123");
        vehicleRequestDTO.setMake("Toyota");
        vehicleRequestDTO.setCapacity(5);
        vehicleRequestDTO.setModelId(1L);

        VehicleResponseDTO vehicleResponseDTO = new VehicleResponseDTO();
        vehicleResponseDTO.setModelName("TestModel");
        vehicleResponseDTO.setId(1L);
        vehicleResponseDTO.setCapacity(3);
        vehicleResponseDTO.setLicensePlate("ABC123");
        vehicleResponseDTO.setMake("Toyota");
        vehicleResponseDTO.setOwner(vehicleOwnerDTO);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setCapacity(3);
        vehicle.setMake("Toyota");
        vehicle.setOwnerId(1L);
        vehicle.setLicensePlate("ABC123");
        vehicle.setModel(vehicleModel);

        when(vehicleModelRepository.findById(1L)).thenReturn(Optional.of(vehicleModel));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleOwnerDTO.class)).thenReturn(Mono.just(vehicleOwnerDTO));

        // Act
        VehicleResponseDTO result = vehicleService.createVehicle(vehicleRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("TestModel", result.getModelName());
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals("Toyota", result.getMake());
        assertEquals(3, result.getCapacity());
        assertEquals(1L, result.getOwner().getId());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(webClient, times(1)).get();
    }

    @Test
    public void testGetAllVehicles() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setLicensePlate("ABC123");
        vehicle.setMake("Toyota");
        vehicle.setCapacity(5);
        vehicle.setOwnerId(1L);
        VehicleModel model = new VehicleModel();
        model.setModelName("Corolla");
        vehicle.setModel(model);

        VehicleOwnerDTO vehicleOwnerDTO = new VehicleOwnerDTO();
        vehicleOwnerDTO.setId(1L);

        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/user/1"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleOwnerDTO.class)).thenReturn(Mono.just(vehicleOwnerDTO));

        // Act
        List<VehicleResponseDTO> vehicleResponseDTOs = vehicleService.getAllVehicles();

        // Assert
        assertEquals(1, vehicleResponseDTOs.size());
        assertEquals("ABC123", vehicleResponseDTOs.get(0).getLicensePlate());
        assertEquals("Toyota", vehicleResponseDTOs.get(0).getMake());

        verify(vehicleRepository, times(1)).findAll();
        verify(webClient, times(1)).get();
    }

    @Test
    public void testGetVehicleById() {
        // Arrange
        Long vehicleId = 1L;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setLicensePlate("ABC123");
        vehicle.setMake("Toyota");
        vehicle.setCapacity(5);
        vehicle.setOwnerId(1L);

        VehicleModel model = new VehicleModel();
        model.setModelName("Corolla");
        vehicle.setModel(model);

        VehicleOwnerDTO vehicleOwnerDTO = new VehicleOwnerDTO();
        vehicleOwnerDTO.setId(1L);

        when(vehicleRepository.getReferenceById(vehicleId)).thenReturn(vehicle);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/user/1"))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleOwnerDTO.class)).thenReturn(Mono.just(vehicleOwnerDTO));

        // Act
        VehicleResponseDTO vehicleResponseDTO = vehicleService.getVehicleById(vehicleId);

        // Assert
        assertEquals("ABC123", vehicleResponseDTO.getLicensePlate());
        assertEquals("Toyota", vehicleResponseDTO.getMake());
        assertEquals(5, vehicleResponseDTO.getCapacity());

        verify(vehicleRepository, times(1)).getReferenceById(vehicleId);
        verify(webClient, times(1)).get();
    }

    @Test
    public void testGetVehiclesByUserId() {
        // Arrange
        Long userId = 1L;
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setMake("Toyota");
        vehicle1.setCapacity(5);
        vehicle1.setOwnerId(userId);
        VehicleModel model1 = new VehicleModel();
        model1.setModelName("Corolla");
        vehicle1.setModel(model1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setId(2L);
        vehicle2.setLicensePlate("DEF456");
        vehicle2.setMake("Honda");
        vehicle2.setCapacity(4);
        vehicle2.setOwnerId(userId);
        VehicleModel model2 = new VehicleModel();
        model2.setModelName("Civic");
        vehicle2.setModel(model2);

        when(vehicleRepository.findByOwnerId(userId)).thenReturn(Arrays.asList(vehicle1, vehicle2));

        // Act
        List<VehicleResponseMinimalDTO> vehicles = vehicleService.getVehiclesByUserId(userId);

        // Assert
        assertEquals(2, vehicles.size());
        assertEquals("ABC123", vehicles.get(0).getLicensePlate());
        assertEquals("DEF456", vehicles.get(1).getLicensePlate());
        assertEquals("Corolla", vehicles.get(0).getModelName());
        assertEquals("Civic", vehicles.get(1).getModelName());

        verify(vehicleRepository, times(1)).findByOwnerId(userId);
    }

    @Test
    public void testCreateVehicleModel() {
        // Arrange
        VehicleModelRequestDTO modelRequestDTO = new VehicleModelRequestDTO();
        modelRequestDTO.setModelName("Corolla");

        VehicleModel model = new VehicleModel();
        model.setId(1L);
        model.setModelName("Corolla");

        when(vehicleModelRepository.save(any(VehicleModel.class))).thenReturn(model);

        // Act
        VehicleModelResponseDTO responseDTO = vehicleService.createVehicleModel(modelRequestDTO);

        // Assert
        assertEquals("Corolla", responseDTO.getModelName());

        verify(vehicleModelRepository, times(1)).save(any(VehicleModel.class));
    }

    @Test
    public void testGetAllVehicleModels() {
        // Arrange
        VehicleModel model = new VehicleModel();
        model.setId(1L);
        model.setModelName("Corolla");

        when(vehicleModelRepository.findAll()).thenReturn(Arrays.asList(model));

        // Act
        List<VehicleModelResponseDTO> modelResponseDTOs = vehicleService.getAllVehicleModels();

        // Assert
        assertEquals(1, modelResponseDTOs.size());
        assertEquals("Corolla", modelResponseDTOs.get(0).getModelName());

        verify(vehicleModelRepository, times(1)).findAll();
    }
}