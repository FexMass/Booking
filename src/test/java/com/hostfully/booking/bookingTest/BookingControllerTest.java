package com.hostfully.booking.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hostfully.booking.booking.BookingController;
import com.hostfully.booking.booking.BookingDTO;
import com.hostfully.booking.booking.BookingService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateBooking() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setCustomerName("John Doe");
        bookingDTO.setStartDateTime(OffsetDateTime.now());
        bookingDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        when(bookingService.createBooking(any(BookingDTO.class))).thenReturn(bookingDTO);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(matchDateTime("$.startDateTime", bookingDTO.getStartDateTime()))
                .andExpect(matchDateTime("$.endDateTime", bookingDTO.getEndDateTime()));

        verify(bookingService, times(1)).createBooking(any(BookingDTO.class));
    }

    @Test
    public void testCreateBooking_ifNullBody() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllBookings() throws Exception {
        BookingDTO booking1 = new BookingDTO();
        booking1.setCustomerName("John Doe");

        OffsetDateTime startDateTime = OffsetDateTime.now();

        booking1.setStartDateTime(startDateTime);
        booking1.setEndDateTime(startDateTime.plusHours(2));

        BookingDTO booking2 = new BookingDTO();
        booking2.setCustomerName("Jane Doe");
        booking2.setStartDateTime(startDateTime.plusDays(1));
        booking2.setEndDateTime(startDateTime.plusDays(1).plusHours(2));

        List<BookingDTO> bookings = Arrays.asList(booking1, booking2);

        given(bookingService.getAllBookings()).willReturn(bookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerName").value(booking1.getCustomerName()))
                .andExpect(matchDateTime("$[0].startDateTime", booking1.getStartDateTime()))
                .andExpect(matchDateTime("$[0].endDateTime", booking1.getEndDateTime()))
                .andExpect(jsonPath("$[1].customerName").value(booking2.getCustomerName()))
                .andExpect(matchDateTime("$[1].startDateTime", booking2.getStartDateTime()))
                .andExpect(matchDateTime("$[1].endDateTime", booking2.getEndDateTime()));
    }

    @Test
    public void testUpdateBooking() throws Exception {
        Long bookingId = 1L;

        BookingDTO updatedBooking = new BookingDTO();
        updatedBooking.setId(bookingId);
        updatedBooking.setCustomerName("Jane Doe");
        updatedBooking.setStartDateTime(OffsetDateTime.now());
        updatedBooking.setEndDateTime(OffsetDateTime.now().plusHours(2));

        when(bookingService.rebookBooking(eq(bookingId), any(BookingDTO.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"))
                .andExpect(matchDateTime("$.startDateTime", updatedBooking.getStartDateTime()))
                .andExpect(matchDateTime("$.endDateTime", updatedBooking.getEndDateTime()));

        verify(bookingService, times(1)).rebookBooking(eq(bookingId), any(BookingDTO.class));
    }

    @Test
    public void testDeleteBooking() throws Exception {
        Long bookingId = 1L;
        doNothing().when(bookingService).cancelBooking(bookingId);

        mockMvc.perform(delete("/api/bookings/{id}", bookingId))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    private ResultMatcher matchDateTime(String jsonPath, OffsetDateTime expectedValue) {
        return result -> {
            String actualValue = JsonPath.read(result.getResponse().getContentAsString(), jsonPath);
            OffsetDateTime actualDateTime = OffsetDateTime.parse(actualValue);
            assertEquals(expectedValue.truncatedTo(ChronoUnit.MILLIS).toInstant(),
                    actualDateTime.truncatedTo(ChronoUnit.MILLIS).toInstant());
        };
    }
}
