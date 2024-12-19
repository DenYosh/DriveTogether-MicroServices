package com.drivetogether.bookingservice.controller;

import com.drivetogether.bookingservice.dto.BookingCarResponseDTO;
import com.drivetogether.bookingservice.dto.BookingRequestDTO;
import com.drivetogether.bookingservice.dto.BookingRequestMinimalDTO;
import com.drivetogether.bookingservice.dto.BookingResponseDTO;
import com.drivetogether.bookingservice.model.Booking;
import com.drivetogether.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequestDTO));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BookingCarResponseDTO>> getBookingByUserId(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingByUserId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
