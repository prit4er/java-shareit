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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void getBookingsByBookerId() throws Exception {
        when(bookingService.getBookingsByBookerIdAndState(BOOKER_ID, BookingStatus.ALL))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings")
                                .header(HEADER_USER_ID, BOOKER_ID)
                                .param("state", "ALL"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].id").value(BOOKING_ID))
               .andExpect(jsonPath("$[0].start").value("2025-03-12T10:00:00"))
               .andExpect(jsonPath("$[0].end").value("2025-03-13T10:00:00"))
               .andExpect(jsonPath("$[0].status").value("WAITING"))
               .andExpect(jsonPath("$[0].booker.id").value(BOOKER_ID))
               .andExpect(jsonPath("$[0].item.id").value(1L));
    }

    @Test
    void getBookingsByOwnerId() throws Exception {
        long ownerId = 2L;
        when(bookingService.getBookingsByOwnerId(ownerId))
                .thenReturn(Collections.singletonList(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                                .header(HEADER_USER_ID, ownerId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getBookingById() throws Exception {
        long userId = 1L;
        long bookingId = 1L;
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{booking-id}", bookingId)
                                .header(HEADER_USER_ID, userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.start").exists());
    }

    @Test
    void createBooking() throws Exception {
        BookingDto inputDto = BookingDto.builder()
                                        .startTime(LocalDateTime.of(2025, 3, 12, 10, 0))
                                        .endTime(LocalDateTime.of(2025, 3, 13, 10, 0))
                                        .itemId(1L)
                                        .build();

        when(bookingService.createBooking(any(BookingDto.class), eq(BOOKER_ID))).thenReturn(bookingDto);

        String jsonRequest = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/bookings")
                                .header(HEADER_USER_ID, BOOKER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(BOOKING_ID))
               .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void approveBooking() throws Exception {
        long ownerId = 2L;
        long bookingId = 1L;
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.approveBooking(bookingId, ownerId, true)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{booking-id}", bookingId)
                                .header(HEADER_USER_ID, ownerId)
                                .param("approved", "true"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
