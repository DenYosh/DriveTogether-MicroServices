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
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookingService, "rideServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    public void testCreateBooking_Success() {
        // Arrange
        String userId = "1";
        String rideId = "1";
        int seatsBooked = 2;
        LocalDateTime bookingTime = LocalDateTime.now();

        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO("1", "1", seatsBooked);
        RideDTO rideDTO = new RideDTO(Long.parseLong(rideId), 5, false);
        Booking booking = new Booking("1", "1", "1", seatsBooked, bookingTime);

        ReflectionTestUtils.setField(bookingService, "rideServiceBaseUrl", "localhost:8082");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8082/api/ride/1")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(RideDTO.class)).thenReturn(Mono.just(rideDTO));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8082/api/ride/1/seatsBooked?delete=false&seatsBooked=2"))
                .thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
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

    @Test
    public void testDeleteBooking_Success() {
        // Arrange
        String bookingId = "booking123";
        Booking booking = new Booking(bookingId, "1", "1", 2, LocalDateTime.now());
        RideDTO rideDTO = new RideDTO(1L, 5, false);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        RequestBodyUriSpec requestBodyUriSpecMock = mock(RequestBodyUriSpec.class);
        RequestBodySpec requestBodySpecMock = mock(RequestBodySpec.class);
        ResponseSpec responseSpecMock = mock(ResponseSpec.class);

        when(webClient.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(RideDTO.class)).thenReturn(Mono.just(rideDTO));

        // Act
        bookingService.deleteBooking(bookingId);

        // Assert
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(webClient, times(1)).put();
        verify(requestBodyUriSpecMock, times(1)).uri(anyString());
        verify(requestBodySpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(RideDTO.class);
    }


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
