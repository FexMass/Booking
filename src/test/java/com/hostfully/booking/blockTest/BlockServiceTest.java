package com.hostfully.booking.blockTest;

import com.hostfully.booking.block.BlockDTO;
import com.hostfully.booking.block.BlockMapper;
import com.hostfully.booking.block.BlockService;
import com.hostfully.booking.repository.Block;
import com.hostfully.booking.repository.BlockRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BlockService blockService;

    private BlockDTO blockDTO;
    private Block block;

    @BeforeEach
    public void setUp() {
        blockDTO = new BlockDTO();
        blockDTO.setId(1L);
        blockDTO.setStartDateTime(OffsetDateTime.now());
        blockDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        block = BlockMapper.toBlockEntity(blockDTO);
    }

    @Test
    public void testCreateBlock() {
        when(bookingRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(bookingRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(blockRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(any(), any()))
                .thenReturn(new ArrayList<>());
        when(blockRepository.save(any(Block.class))).thenReturn(block);

        BlockDTO createdBlockDTO = blockService.createBlock(blockDTO);

        assertEquals(blockDTO.getStartDateTime(), createdBlockDTO.getStartDateTime());
        assertEquals(blockDTO.getEndDateTime(), createdBlockDTO.getEndDateTime());

        verify(blockRepository, times(1)).save(any(Block.class));
    }

    @Test
    public void testGetAllBlocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(block);

        when(blockRepository.findAll()).thenReturn(blocks);

        List<BlockDTO> blockDTOList = blockService.getAllBlocks();

        assertEquals(1, blockDTOList.size());
        assertEquals(blockDTO.getStartDateTime(), blockDTOList.get(0).getStartDateTime());
        assertEquals(blockDTO.getEndDateTime(), blockDTOList.get(0).getEndDateTime());

        verify(blockRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteBlock() {
        doNothing().when(blockRepository).deleteById(block.getId());

        blockService.deleteBlock(block.getId());

        verify(blockRepository, times(1)).deleteById(block.getId());
    }
}
