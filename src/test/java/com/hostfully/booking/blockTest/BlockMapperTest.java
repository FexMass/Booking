package com.hostfully.booking.blockTest;

import com.hostfully.booking.block.BlockDTO;
import com.hostfully.booking.block.BlockMapper;
import com.hostfully.booking.repository.Block;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BlockMapperTest {

    @Test
    public void testToBlockDto() {
        Block block = new Block();
        block.setId(1L);
        block.setStartDateTime(OffsetDateTime.now());
        block.setEndDateTime(OffsetDateTime.now().plusHours(2));

        BlockDTO blockDTO = BlockMapper.toBlockDto(block);

        assertEquals(block.getId(), blockDTO.getId());
        assertEquals(block.getStartDateTime(), blockDTO.getStartDateTime());
        assertEquals(block.getEndDateTime(), blockDTO.getEndDateTime());
    }

    @Test
    public void testToBlockEntity() {
        BlockDTO blockDTO = new BlockDTO();
        blockDTO.setId(1L);
        blockDTO.setStartDateTime(OffsetDateTime.now());
        blockDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        Block block = BlockMapper.toBlockEntity(blockDTO);

        assertEquals(blockDTO.getId(), block.getId());
        assertEquals(blockDTO.getStartDateTime(), block.getStartDateTime());
        assertEquals(blockDTO.getEndDateTime(), block.getEndDateTime());
    }

    @Test
    public void testToBlockDto_NullInput() {
        assertNull(BlockMapper.toBlockDto(null));
    }

    @Test
    public void testToBlockEntity_NullInput() {
        assertNull(BlockMapper.toBlockEntity(null));
    }
}
