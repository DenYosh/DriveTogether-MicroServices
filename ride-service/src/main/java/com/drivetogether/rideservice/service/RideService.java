package com.drivetogether.rideservice.service;

import com.drivetogether.rideservice.model.Ride;
import com.drivetogether.rideservice.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RideService {

    private RideRepository rideRepository;

    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public List<Ride> searchRides(String source, String destination) {
        return rideRepository.findBySourceAndDestination(source, destination);
    }

    public void deleteRide(Long id) {
        rideRepository.deleteById(id);
    }
}
