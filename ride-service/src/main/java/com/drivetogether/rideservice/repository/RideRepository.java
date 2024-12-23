package com.drivetogether.rideservice.repository;

import com.drivetogether.rideservice.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findBySourceAndDestination(String source, String destination);
}
