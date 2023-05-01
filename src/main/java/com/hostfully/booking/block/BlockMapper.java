package com.hostfully.booking.block;

import com.hostfully.booking.repository.Block;

public class BlockMapper {
    public static BlockDTO toBlockDto(Block block) {
        if (block == null) {
            return null;
        }
        BlockDTO dto = new BlockDTO();
        dto.setId(block.getId());
        dto.setStartDateTime(block.getStartDateTime());
        dto.setEndDateTime(block.getEndDateTime());
        return dto;
    }

    public static Block toBlockEntity(BlockDTO dto) {
        if (dto == null) {
            return null;
        }
        Block booking = new Block();
        booking.setId(dto.getId());
        booking.setStartDateTime(dto.getStartDateTime());
        booking.setEndDateTime(dto.getEndDateTime());
        return booking;
    }
}
