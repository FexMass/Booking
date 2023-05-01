package com.hostfully.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    List<Block> findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(OffsetDateTime endDateTime, OffsetDateTime startDateTime);
}
