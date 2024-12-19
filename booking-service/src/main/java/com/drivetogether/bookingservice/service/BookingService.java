package com.drivetogether.bookingservice.service;

import com.drivetogether.bookingservice.dto.*;
import com.drivetogether.bookingservice.model.Booking;
import com.drivetogether.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WebClient webClient;

    @Value("${rideservice.baseurl}")
    private String rideServiceBaseUrl;

    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        RideDTO rideDTO = webClient.get()
                .uri("http://" + rideServiceBaseUrl + "/api/ride/" + bookingRequestDTO.getRideId())
                .retrieve()
                .bodyToMono(RideDTO.class)
                .block();

        if (rideDTO != null) {

            if (rideDTO.isCompleted()) {
                throw new RuntimeException("Booking is completed");
            }

            if (rideDTO.getAvailableSeats() - bookingRequestDTO.getSeatsBooked() < 0) {
                throw new RuntimeException("No seats available");
            }

            webClient.put()
                    .uri("http://" + rideServiceBaseUrl + "/api/ride/" + bookingRequestDTO.getRideId() + "/seatsBooked?delete=false&seatsBooked=" + bookingRequestDTO.getSeatsBooked())
                    .retrieve()
                    .bodyToMono(RideDTO.class)
                    .block();

            Booking booking = Booking.builder()
                    .userId(bookingRequestDTO.getUserId())
                    .rideId(bookingRequestDTO.getRideId())
                    .seatsBooked(bookingRequestDTO.getSeatsBooked())
                    .bookingTime(LocalDateTime.now())
                    .build();
            booking = bookingRepository.save(booking);
            return mapToResponseDTO(booking);
        }

        throw new RuntimeException("No ride found.");
    }

    public void deleteBooking(String id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));

        webClient.put()
                .uri("http://" + rideServiceBaseUrl + "/api/ride/" + booking.getRideId() + "/seatsBooked?delete=true&seatsBooked=" + booking.getSeatsBooked())
                .retrieve()
                .bodyToMono(RideDTO.class)
                .block();

        bookingRepository.delete(booking);
    }

    public List<BookingCarResponseDTO> getBookingByUserId(String id) {
        List<Booking> bookings = bookingRepository.findAllByUserId(id);

        return bookings.stream()
                .map(this::mapToCarResponseDTO)
                .collect(Collectors.toList());
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

    private BookingCarResponseDTO mapToCarResponseDTO(Booking booking) {
        RideFullDTO rideDTO = webClient.get()
                .uri("http://" + rideServiceBaseUrl + "/api/ride/" + booking.getRideId())
                .retrieve()
                .bodyToMono(RideFullDTO.class)
                .block();

        return BookingCarResponseDTO.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .ride(rideDTO)
                .seatsBooked(booking.getSeatsBooked())
                .bookingTime(booking.getBookingTime())
                .build();

    }

}
