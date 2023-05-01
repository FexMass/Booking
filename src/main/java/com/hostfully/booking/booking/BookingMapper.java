package com.hostfully.booking.booking;

import com.hostfully.booking.repository.Booking;

public class BookingMapper {
    public static BookingDTO toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setStartDateTime(booking.getStartDateTime());
        dto.setEndDateTime(booking.getEndDateTime());
        dto.setCustomerName(booking.getCustomerName());
        return dto;
    }

    public static Booking toBookingEntity(BookingDTO dto) {
        if (dto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStartDateTime(dto.getStartDateTime());
        booking.setEndDateTime(dto.getEndDateTime());
        booking.setCustomerName(dto.getCustomerName());
        return booking;
    }
}
