package com.hostfully.booking.booking;

import java.util.List;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/bookings")
    @ApiOperation(value = "Create a new booking", response = BookingDTO.class)
    public ResponseEntity<BookingDTO> createBooking(@RequestBody @NonNull BookingDTO booking) {
        BookingDTO createdBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/bookings")
    @ApiOperation(value = "Get all bookings", response = List.class)
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping("/bookings/{id}")
    @ApiOperation(value = "Update a booking by ID", response = BookingDTO.class)
    public ResponseEntity<BookingDTO> updateBooking(
            @ApiParam(value = "Booking ID") @PathVariable Long id, @RequestBody BookingDTO booking) {
        BookingDTO updatedBooking = bookingService.rebookBooking(id, booking);
        return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
    }

    @DeleteMapping("/bookings/{id}")
    @ApiOperation(value = "Delete a booking by ID")
    public ResponseEntity<Void> deleteBooking(@ApiParam(value = "Booking ID") @PathVariable Long id) {
        bookingService.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
