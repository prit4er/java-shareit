package ru.practicum.shareit.ru.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private static final long BOOKER_ID = 1L;
    private static final long OWNER_ID = 2L;
    private static final long BOOKING_ID = 1L;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        bookingDto = BookingDto.builder()
                               .id(BOOKING_ID)
                               .startTime(LocalDateTime.of(2025, 3, 12, 10, 0))
                               .endTime(LocalDateTime.of(2025, 3, 13, 10, 0))
                               .itemId(1L)
                               .status(BookingStatus.WAITING)
                               .booker(new UserDto(BOOKER_ID, "John", "john@example.com"))
                               .item(ItemDto.builder()
                                            .id(1L)
                                            .name("Hammer")
                                            .description("A hammer")
                                            .available(true)
                                            .owner(OWNER_ID)
                                            .build())
                               .build();
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void shouldReturnBookingsForCurrentState() throws Exception {
        when(bookingService.getBookingsByBookerIdAndState(BOOKER_ID, BookingStatus.CURRENT))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                                .header(HEADER_USER_ID, BOOKER_ID)
                                .param("state", "CURRENT"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(BOOKING_ID))
               .andExpect(jsonPath("$[0].status").value("WAITING"));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        BookingDto inputDto = BookingDto.builder()
                                        .startTime(LocalDateTime.of(2025, 3, 12, 10, 0))
                                        .endTime(LocalDateTime.of(2025, 3, 13, 10, 0))
                                        .itemId(1L)
                                        .build();

        when(bookingService.createBooking(any(BookingDto.class), eq(BOOKER_ID))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                                .header(HEADER_USER_ID, BOOKER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(inputDto)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(BOOKING_ID))
               .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void shouldApproveBooking() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.approveBooking(BOOKING_ID, OWNER_ID, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{booking-id}", BOOKING_ID)
                                .header(HEADER_USER_ID, OWNER_ID)
                                .param("approved", "true"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(BOOKING_ID))
               .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}