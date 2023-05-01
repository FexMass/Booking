package com.hostfully.booking.bookingTest;

import com.hostfully.booking.repository.Booking;
import com.hostfully.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    private Booking booking;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
        booking.setCustomerName("John Doe");
        booking.setStartDateTime(OffsetDateTime.now());
        booking.setEndDateTime(OffsetDateTime.now().plusHours(2));
        booking = bookingRepository.save(booking);
    }

    @Test
    public void testFindAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual() {
        List<Booking> bookings = bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                booking.getStartDateTime().minusHours(1), booking.getEndDateTime().plusHours(1));

        assertEquals(1, bookings.size());
        assertEquals(booking.getCustomerName(), bookings.get(0).getCustomerName());
    }

    @Test
    public void testFindAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual() {
        List<Booking> bookings = bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                booking.getEndDateTime().minusHours(1), booking.getStartDateTime().plusHours(1));

        assertEquals(1, bookings.size());
        assertEquals(booking.getCustomerName(), bookings.get(0).getCustomerName());
    }

    @Test
    public void testFindAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual_NoResults() {
        List<Booking> bookings = bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                booking.getStartDateTime().plusHours(3), booking.getEndDateTime().plusHours(5));

        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testFindAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual_NoResults() {
        List<Booking> bookings = bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                booking.getEndDateTime().plusHours(1), booking.getStartDateTime().plusHours(3));

        assertTrue(bookings.isEmpty());
    }
}
