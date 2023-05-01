package com.hostfully.booking.bookingTest;

import com.hostfully.booking.repository.BlockRepository;
import com.hostfully.booking.booking.*;
import com.hostfully.booking.repository.Booking;
import com.hostfully.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BookingService bookingService;

    private BookingDTO bookingDTO;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setCustomerName("John Doe");
        bookingDTO.setStartDateTime(OffsetDateTime.now());
        bookingDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        booking = BookingMapper.toBookingEntity(bookingDTO);
    }

    @Test
    public void testCreateBooking() {
        when(bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDTO createdBookingDTO = bookingService.createBooking(bookingDTO);

        assertEquals(bookingDTO.getCustomerName(), createdBookingDTO.getCustomerName());
        assertEquals(bookingDTO.getStartDateTime(), createdBookingDTO.getStartDateTime());
        assertEquals(bookingDTO.getEndDateTime(), createdBookingDTO.getEndDateTime());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCancelBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).deleteById(booking.getId());

        bookingService.cancelBooking(booking.getId());

        verify(bookingRepository, times(1)).deleteById(booking.getId());
    }

    @Test
    public void testGetAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findAll()).thenReturn(bookings);

        List<BookingDTO> bookingDTOList = bookingService.getAllBookings();

        assertEquals(1, bookingDTOList.size());
        assertEquals(bookingDTO.getCustomerName(), bookingDTOList.get(0).getCustomerName());

        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    public void testRebookBooking() {
        BookingDTO updatedBookingDTO = new BookingDTO();
        updatedBookingDTO.setId(1L);
        updatedBookingDTO.setCustomerName("Jane Doe");
        updatedBookingDTO.setStartDateTime(OffsetDateTime.now().plusDays(1));
        updatedBookingDTO.setEndDateTime(OffsetDateTime.now().plusDays(1).plusHours(2));

        Booking updatedBooking = BookingMapper.toBookingEntity(updatedBookingDTO);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(updatedBooking.getStartDateTime(), updatedBooking.getEndDateTime()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(updatedBooking.getEndDateTime(), updatedBooking.getStartDateTime()))
                .thenReturn(new ArrayList<>());
        when(blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(updatedBooking.getStartDateTime(), updatedBooking.getEndDateTime()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.save(booking)).thenReturn(updatedBooking);

        BookingDTO rebookedBookingDTO = bookingService.rebookBooking(booking.getId(), updatedBookingDTO);

        assertEquals(updatedBookingDTO.getCustomerName(), rebookedBookingDTO.getCustomerName());
        assertEquals(updatedBookingDTO.getStartDateTime(), rebookedBookingDTO.getStartDateTime());
        assertEquals(updatedBookingDTO.getEndDateTime(), rebookedBookingDTO.getEndDateTime());

        verify(bookingRepository, times(1)).save(booking);
    }
}
