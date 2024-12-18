package com.drivetogether.bookingservice;

import com.drivetogether.bookingservice.dto.BookingRequestDTO;
import com.drivetogether.bookingservice.dto.BookingResponseDTO;
import com.drivetogether.bookingservice.dto.RideDTO;
import com.drivetogether.bookingservice.model.Booking;
import com.drivetogether.bookingservice.repository.BookingRepository;
import com.drivetogether.bookingservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceUnitTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookingService, "rideServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    public void testCreateBooking_Success() {
        // Arrange
        Long userId = 1L;
        Long rideId = 1L;
        int seatsBooked = 2;
        LocalDateTime bookingTime = LocalDateTime.now();

        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO("1", "1", seatsBooked);
        RideDTO rideDTO = new RideDTO(rideId, 5, false);
        Booking booking = new Booking("1", "1", "1", seatsBooked, bookingTime);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RideDTO.class)).thenReturn(Mono.just(rideDTO));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        BookingResponseDTO responseDTO = bookingService.createBooking(bookingRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals("1", responseDTO.getId());
        assertEquals(userId, responseDTO.getUserId());
        assertEquals(rideId, responseDTO.getRideId());
        assertEquals(seatsBooked, responseDTO.getSeatsBooked());

        // Verify that the save method was called once on the repository
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCreateBooking_RideCompleted() {
        // Arrange
        String userId = "1";
        String rideId = "1";
        int seatsBooked = 2;

        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(userId, rideId, seatsBooked);
        RideDTO rideDTO = new RideDTO(1L, 5, true);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RideDTO.class)).thenReturn(Mono.just(rideDTO));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingRequestDTO));
        assertEquals("Booking is completed", exception.getMessage());
    }

    @Test
    public void testCreateBooking_NoSeatsAvailable() {
        // Arrange
        String userId = "1";
        String rideId = "1";
        int seatsBooked = 10;
        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO(userId, rideId, seatsBooked);
        RideDTO rideDTO = new RideDTO(1L, 5, false);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RideDTO.class)).thenReturn(Mono.just(rideDTO));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingRequestDTO));
        assertEquals("No seats available", exception.getMessage());
    }

//    @Test
//    public void testDeleteBooking_Success() {
//        // Arrange
//        String bookingId = "booking123";
//        Booking booking = new Booking(bookingId, "1", "1", 2, LocalDateTime.now());
//
//        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.of(booking));
//        when(webClient.put()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(RideDTO.class)).thenReturn(Mono.just(new RideDTO(1L, 5, false)));
//
//        // Act
//        bookingService.deleteBooking(bookingId);
//
//        // Assert
//        verify(bookingRepository, times(1)).findById(bookingId);
//        verify(webClient, times(1)).put();
//    }

    @Test
    public void testDeleteBooking_BookingNotFound() {
        // Arrange
        String bookingId = "booking123";
        when(bookingRepository.findById(bookingId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookingService.deleteBooking(bookingId));
        assertEquals("Id not found", exception.getMessage());
    }
}
