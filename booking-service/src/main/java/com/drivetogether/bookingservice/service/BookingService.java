package com.drivetogether.bookingservice.service;

import com.drivetogether.bookingservice.model.Booking;
import com.drivetogether.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }
}
