package com.hostfully.booking.bookingTest;

import com.hostfully.booking.repository.Booking;
import com.hostfully.booking.booking.BookingDTO;
import com.hostfully.booking.booking.BookingMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookingMapperTest {

    @Test
    public void testToBookingDto() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerName("John Doe");
        booking.setStartDateTime(OffsetDateTime.now());
        booking.setEndDateTime(OffsetDateTime.now().plusHours(2));

        BookingDTO bookingDTO = BookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDTO.getId());
        assertEquals(booking.getCustomerName(), bookingDTO.getCustomerName());
        assertEquals(booking.getStartDateTime(), bookingDTO.getStartDateTime());
        assertEquals(booking.getEndDateTime(), bookingDTO.getEndDateTime());
    }

    @Test
    public void testToBookingEntity() {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setCustomerName("John Doe");
        bookingDTO.setStartDateTime(OffsetDateTime.now());
        bookingDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        Booking booking = BookingMapper.toBookingEntity(bookingDTO);

        assertEquals(bookingDTO.getId(), booking.getId());
        assertEquals(bookingDTO.getCustomerName(), booking.getCustomerName());
        assertEquals(bookingDTO.getStartDateTime(), booking.getStartDateTime());
        assertEquals(bookingDTO.getEndDateTime(), booking.getEndDateTime());
    }

    @Test
    public void testToBookingDto_NullInput() {
        assertNull(BookingMapper.toBookingDto(null));
    }

    @Test
    public void testToBookingEntity_NullInput() {
        assertNull(BookingMapper.toBookingEntity(null));
    }
}
