package com.drivetogether.rideservice.controller;

import com.drivetogether.rideservice.dto.RideRequestDTO;
import com.drivetogether.rideservice.dto.RideResponseDTO;
import com.drivetogether.rideservice.service.RideService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideResponseDTO> createRide(@RequestBody RideRequestDTO rideRequestDTO) {
        return ResponseEntity.ok(rideService.createRide(rideRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<RideResponseDTO> completeRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideService.completeRide(rideId));
    }

    @PutMapping("/{rideId}/seatsBooked")
    public ResponseEntity<RideResponseDTO> updateSeatsBooked(@PathVariable Long rideId, @PathParam("seats") Integer seatsBooked, @PathParam("delete") Boolean delete) {
        return ResponseEntity.ok(rideService.updateSeatsBooked(rideId, seatsBooked , delete));
    }
}
