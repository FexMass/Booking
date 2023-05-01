package com.hostfully.booking.booking;

import com.hostfully.booking.repository.Block;
import com.hostfully.booking.exception.ExceptionManager;
import com.hostfully.booking.repository.BlockRepository;
import com.hostfully.booking.repository.Booking;
import com.hostfully.booking.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    public BookingService(BookingRepository bookingRepository, BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        checkConflictsAndBlocks(bookingDTO.getStartDateTime(), bookingDTO.getEndDateTime());
        Booking booking = BookingMapper.toBookingEntity(bookingDTO);
        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(savedBooking);
    }

    public void cancelBooking(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isEmpty()) {
            throw new ExceptionManager.NotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Transactional
    public BookingDTO rebookBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found."));

        checkConflictsAndBlocks(bookingDTO.getStartDateTime(), bookingDTO.getEndDateTime(), id);

        booking.setStartDateTime(bookingDTO.getStartDateTime());
        booking.setEndDateTime(bookingDTO.getEndDateTime());
        booking.setCustomerName(bookingDTO.getCustomerName());

        Booking savedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(savedBooking);
    }

    private void checkConflictsAndBlocks(OffsetDateTime startDateTime, OffsetDateTime endDateTime, Long... excludeIds) {
        List<Booking> conflictingBookings = bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime);
        List<Booking> conflictingBookingsBetween = bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(endDateTime, startDateTime);

        if (excludeIds.length > 0) {
            Long excludeId = excludeIds[0];
            conflictingBookings.removeIf(b -> b.getId().equals(excludeId));
            conflictingBookingsBetween.removeIf(b -> b.getId().equals(excludeId));
        }

        if (!conflictingBookings.isEmpty() || !conflictingBookingsBetween.isEmpty()) {
            throw new ExceptionManager.conflictException("There is already a booking during the requested time period.");
        }

        List<Block> blocks = blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime);
        List<Block> blocksBetween = blockRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(endDateTime, startDateTime);

        if (!blocks.isEmpty() || !blocksBetween.isEmpty()) {
            throw new ExceptionManager.conflictException("The requested time period is blocked.");
        }
    }
}
