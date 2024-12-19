package com.drivetogether.bookingservice.repository;

import com.drivetogether.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findAllByUserId(String userId);
}
