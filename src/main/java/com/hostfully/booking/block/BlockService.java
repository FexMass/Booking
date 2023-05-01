package com.hostfully.booking.block;

import com.hostfully.booking.repository.Booking;
import com.hostfully.booking.exception.ExceptionManager;
import com.hostfully.booking.repository.BookingRepository;
import com.hostfully.booking.repository.Block;
import com.hostfully.booking.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final BookingRepository bookingRepository;

    public BlockService(BlockRepository blockRepository, BookingRepository bookingRepository) {
        this.blockRepository = blockRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<BlockDTO> getAllBlocks() {
        List<Block> blocks = blockRepository.findAll();
        return blocks.stream().map(BlockMapper::toBlockDto).collect(Collectors.toList());
    }

    public BlockDTO createBlock(BlockDTO blockDto) {
        checkConflictsAndBlocks(blockDto.getStartDateTime(), blockDto.getEndDateTime());
        Block block = BlockMapper.toBlockEntity(blockDto);
        Block savedBlock = blockRepository.save(block);
        return BlockMapper.toBlockDto(savedBlock);
    }

    public void deleteBlock(Long blockId) {
        blockRepository.deleteById(blockId);
    }

    private void checkConflictsAndBlocks(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Booking> conflictingBookings = bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime);
        List<Booking> conflictingBookingsBetween = bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(endDateTime, startDateTime);

        if (!conflictingBookings.isEmpty() || !conflictingBookingsBetween.isEmpty()) {
            throw new ExceptionManager.conflictException("The requested time period is booked.");
        }

        List<Block> blocks = blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDateTime, endDateTime);
        List<Block> blocksBetween = blockRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(startDateTime, endDateTime);

        if (!blocks.isEmpty() || !blocksBetween.isEmpty()) {
            throw new ExceptionManager.conflictException("There is already a block during the requested time period.");
        }
    }
}
