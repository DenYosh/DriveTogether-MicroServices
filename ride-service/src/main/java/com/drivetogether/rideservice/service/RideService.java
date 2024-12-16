package com.drivetogether.rideservice.service;

import com.drivetogether.rideservice.dto.RideRequestDTO;
import com.drivetogether.rideservice.dto.RideResponseDTO;
import com.drivetogether.rideservice.dto.VehicleDTO;
import com.drivetogether.rideservice.dto.VehicleOwnerDTO;
import com.drivetogether.rideservice.model.Ride;
import com.drivetogether.rideservice.repository.RideRepository;
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
public class RideService {

    private final RideRepository rideRepository;
    private final WebClient webClient;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    @Value("${vehicleservice.baseurl}")
    private String vehicleServiceBaseUrl;

    public RideResponseDTO createRide(RideRequestDTO rideRequestDTO) {
        Ride ride = Ride.builder()
                .carId(rideRequestDTO.getCarId())
                .source(rideRequestDTO.getSource())
                .destination(rideRequestDTO.getDestination())
                .startTime(rideRequestDTO.getStartTime())
                .endTime(rideRequestDTO.getEndTime())
                .availableSeats(rideRequestDTO.getAvailableSeats())
                .completed(false)
                .build();
        ride = rideRepository.save(ride);
        return mapToResponseDTO(ride);
    }

    public List<RideResponseDTO> getAllRides() {
        return rideRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public RideResponseDTO completeRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        ride.setCompleted(true);
        ride = rideRepository.save(ride);
        return mapToResponseDTO(ride);
    }

    private RideResponseDTO mapToResponseDTO(Ride ride) {
        VehicleDTO vehicleDTO = null;
        if (ride.getCarId() != null) {
            vehicleDTO = webClient.get()
                    .uri("http://" + vehicleServiceBaseUrl + "/api/vehicle/" + ride.getCarId())
                    .retrieve()
                    .bodyToMono(VehicleDTO.class)
                    .block();
        }

        return RideResponseDTO.builder()
                .id(ride.getId())
                .vehicle(vehicleDTO)
                .source(ride.getSource())
                .destination(ride.getDestination())
                .startTime(ride.getStartTime())
                .endTime(ride.getEndTime())
                .availableSeats(ride.getAvailableSeats())
                .completed(ride.getCompleted())
                .build();
    }
}
