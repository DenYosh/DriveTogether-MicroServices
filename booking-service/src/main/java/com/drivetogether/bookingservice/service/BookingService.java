package com.drivetogether.bookingservice.service;

import com.drivetogether.bookingservice.dto.BookingRequestDTO;
import com.drivetogether.bookingservice.dto.BookingResponseDTO;
import com.drivetogether.bookingservice.model.Booking;
import com.drivetogether.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        Booking booking = Booking.builder()
                .userId(bookingRequestDTO.getUserId())
                .rideId(bookingRequestDTO.getRideId())
                .seatsBooked(bookingRequestDTO.getSeatsBooked())
                .bookingTime(LocalDateTime.now())
                .build();
        booking = bookingRepository.save(booking);
        return mapToResponseDTO(booking);
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }

    public BookingResponseDTO updateBooking(String id, BookingRequestDTO bookingRequestDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setUserId(bookingRequestDTO.getUserId());
        booking.setRideId(bookingRequestDTO.getRideId());
        booking.setSeatsBooked(bookingRequestDTO.getSeatsBooked());
        booking = bookingRepository.save(booking);
        return mapToResponseDTO(booking);
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .rideId(booking.getRideId())
                .seatsBooked(booking.getSeatsBooked())
                .bookingTime(booking.getBookingTime())
                .build();
    }
}
