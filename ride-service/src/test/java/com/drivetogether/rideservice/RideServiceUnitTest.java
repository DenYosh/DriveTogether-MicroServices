package com.drivetogether.rideservice;

import com.drivetogether.rideservice.dto.RideRequestDTO;
import com.drivetogether.rideservice.dto.RideResponseDTO;
import com.drivetogether.rideservice.dto.VehicleDTO;
import com.drivetogether.rideservice.model.Ride;
import com.drivetogether.rideservice.repository.RideRepository;
import com.drivetogether.rideservice.service.RideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceUnitTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @InjectMocks
    private RideService rideService;

    @Test
    void testCreateRide_Success() {
        RideRequestDTO rideRequestDTO = RideRequestDTO.builder()
                .carId(1L)
                .source("Source")
                .destination("Destination")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .availableSeats(4)
                .build();

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .id(1L)
                .capacity(5)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleDTO.class)).thenReturn(Mono.just(vehicleDTO));

        Ride ride = Ride.builder()
                .id(1L)
                .carId(1L)
                .source("Source")
                .destination("Destination")
                .startTime(rideRequestDTO.getStartTime())
                .endTime(rideRequestDTO.getEndTime())
                .availableSeats(rideRequestDTO.getAvailableSeats())
                .completed(false)
                .build();

        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        // Act
        RideResponseDTO result = rideService.createRide(rideRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Source", result.getSource());
        assertEquals("Destination", result.getDestination());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    void testCreateRide_VehicleNotFound() {
        RideRequestDTO rideRequestDTO = RideRequestDTO.builder()
                .carId(1L)
                .source("Source")
                .destination("Destination")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .availableSeats(4)
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleDTO.class)).thenReturn(Mono.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> rideService.createRide(rideRequestDTO));
        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    public void testGetAllRides() {
        // Arrange
        Ride ride1 = Ride.builder()
                .id(1L)
                .carId(1L)
                .source("Hasselt")
                .destination("Geel")
                .startTime(LocalDateTime.of(2024, 12, 19, 19, 0))
                .endTime(LocalDateTime.of(2024, 12, 19, 20, 0))
                .availableSeats(4)
                .completed(false)
                .build();
        Ride ride2 = Ride.builder()
                .id(2L)
                .carId(2L)
                .source("Brussel")
                .destination("Antwerpen")
                .startTime(LocalDateTime.of(2024, 12, 20, 10, 0))
                .endTime(LocalDateTime.of(2024, 12, 20, 11, 30))
                .availableSeats(2)
                .completed(true)
                .build();
        List<Ride> rides = Arrays.asList(ride1, ride2);
        when(rideRepository.findAll()).thenReturn(rides);

        VehicleDTO vehicleDTO1 = VehicleDTO.builder().id(1L).capacity(5).build();
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://null/api/vehicle/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleDTO.class)).thenReturn(Mono.just(vehicleDTO1));

        VehicleDTO vehicleDTO2 = VehicleDTO.builder().id(2L).capacity(4).build();
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://null/api/vehicle/2")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(VehicleDTO.class)).thenReturn(Mono.just(vehicleDTO2));

        //Act
        List<RideResponseDTO> result = rideService.getAllRides();

        //Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Hasselt", result.get(0).getSource());
        assertEquals("Geel", result.get(0).getDestination());
        assertEquals(LocalDateTime.of(2024, 12, 19, 19, 0), result.get(0).getStartTime());
        assertEquals(LocalDateTime.of(2024, 12, 19, 20, 0), result.get(0).getEndTime());
        assertEquals(4, result.get(0).getAvailableSeats());
        assertFalse(result.get(0).getCompleted());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Brussel", result.get(1).getSource());
        assertEquals("Antwerpen", result.get(1).getDestination());
        assertEquals(LocalDateTime.of(2024, 12, 20, 10, 0), result.get(1).getStartTime());
        assertEquals(LocalDateTime.of(2024, 12, 20, 11, 30), result.get(1).getEndTime());
        assertEquals(2, result.get(1).getAvailableSeats());
        assertTrue(result.get(1).getCompleted());

        verify(rideRepository, times(1)).findAll();
        verify(webClient, times(2)).get();
    }

    @Test
    void testCompleteRide() {
        Ride ride = Ride.builder()
                .id(1L)
                .completed(false)
                .build();
        Ride savedRide = Ride.builder()
                .id(1L)
                .completed(true)
                .build();

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        // Act
        RideResponseDTO result = rideService.completeRide(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getCompleted());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    void testUpdateSeatsBooked_Success() {
        Ride ride = Ride.builder()
                .id(1L)
                .availableSeats(4)
                .build();
        Ride savedRide = Ride.builder()
                .id(1L)
                .availableSeats(2)
                .build();

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(savedRide);

        // Act
        RideResponseDTO result = rideService.updateSeatsBooked(1L, 2, false);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getAvailableSeats());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    void testUpdateSeatsBooked_NoSeatsAvailable() {
        Ride ride = Ride.builder()
                .id(1L)
                .availableSeats(1)
                .build();

        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class,
                () -> rideService.updateSeatsBooked(1L, 2, false));
        assertEquals("No seats available", exception.getMessage());
    }
}