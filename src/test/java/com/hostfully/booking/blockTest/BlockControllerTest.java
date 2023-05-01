package com.hostfully.booking.blockTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hostfully.booking.block.BlockController;
import com.hostfully.booking.block.BlockDTO;
import com.hostfully.booking.block.BlockService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BlockControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BlockController blockController;

    @Mock
    private BlockService blockService;

    private BlockDTO blockDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blockController).build();
        blockDTO = new BlockDTO();
        blockDTO.setId(1L);
        blockDTO.setStartDateTime(OffsetDateTime.now(ZoneOffset.UTC));
        blockDTO.setEndDateTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(2));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCreateBlock() throws Exception {
        BlockDTO blockDTO = new BlockDTO();
        blockDTO.setStartDateTime(OffsetDateTime.now());
        blockDTO.setEndDateTime(OffsetDateTime.now().plusHours(2));

        given(blockService.createBlock(any(BlockDTO.class))).willReturn(blockDTO);

        mockMvc.perform(post("/api/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(matchDateTime("$.startDateTime", blockDTO.getStartDateTime()))
                .andExpect(matchDateTime("$.endDateTime", blockDTO.getEndDateTime()));
    }

    @Test
    public void testGetAllBlocks() throws Exception {
        List<BlockDTO> blockDTOList = new ArrayList<>();
        blockDTOList.add(blockDTO);

        when(blockService.getAllBlocks()).thenReturn(blockDTOList);

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(matchDateTime("$[0].startDateTime", blockDTO.getStartDateTime()))
                .andExpect(matchDateTime("$[0].endDateTime", blockDTO.getEndDateTime()));

        verify(blockService, times(1)).getAllBlocks();
    }

    @Test
    public void testDeleteBlock() throws Exception {
        doNothing().when(blockService).deleteBlock(anyLong());

        mockMvc.perform(delete("/api/blocks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(blockService, times(1)).deleteBlock(1L);
    }

    private ResultMatcher matchDateTime(String jsonPath, OffsetDateTime expectedValue) {
        return result -> {
            String actualValue = JsonPath.read(result.getResponse().getContentAsString(), jsonPath);
            OffsetDateTime actualDateTime = OffsetDateTime.parse(actualValue);
            assertEquals(expectedValue.truncatedTo(ChronoUnit.MILLIS).toInstant(),
                    actualDateTime.truncatedTo(ChronoUnit.MILLIS).toInstant());
        };
    }
}
