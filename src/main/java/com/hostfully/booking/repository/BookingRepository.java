package com.hostfully.booking.repository;

import com.hostfully.booking.repository.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    List<Booking> findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(OffsetDateTime endDateTime, OffsetDateTime startDateTime);
}
