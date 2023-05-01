package com.hostfully.booking.blockTest;

import com.hostfully.booking.repository.Block;
import com.hostfully.booking.repository.BlockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    private Block block;

    @BeforeEach
    public void setUp() {
        block = new Block();
        block.setStartDateTime(OffsetDateTime.now());
        block.setEndDateTime(OffsetDateTime.now().plusHours(2));
        block = blockRepository.save(block);
    }

    @Test
    public void testFindAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual() {
        List<Block> blocks = blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                block.getStartDateTime().minusMinutes(1),
                block.getEndDateTime().plusMinutes(1)
        );
        assertEquals(1, blocks.size());
        assertEquals(block.getId(), blocks.get(0).getId());
    }

    @Test
    public void testFindAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual() {
        List<Block> blocks = blockRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                block.getEndDateTime().plusMinutes(1),
                block.getStartDateTime().minusMinutes(1)
        );
        assertEquals(1, blocks.size());
        assertEquals(block.getId(), blocks.get(0).getId());
    }
    @Test
    public void testFindAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual_NoResults() {
        List<Block> bookings = blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(
                block.getStartDateTime().plusHours(3), block.getEndDateTime().plusHours(5));

        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testFindAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual_NoResults() {
        List<Block> bookings = blockRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                block.getEndDateTime().plusHours(1), block.getStartDateTime().plusHours(3));

        assertTrue(bookings.isEmpty());
    }
}
