package com.hostfully.booking.block;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping("/blocks")
    @ApiOperation(value = "Create a new block", response = BlockDTO.class)
    public ResponseEntity<BlockDTO> createBlock(@RequestBody BlockDTO blockDTO) {
        BlockDTO createdBlock = blockService.createBlock(blockDTO);
        return new ResponseEntity<>(createdBlock, HttpStatus.CREATED);
    }

    @GetMapping("/blocks")
    @ApiOperation(value = "Get all blocks", response = List.class)
    public ResponseEntity<List<BlockDTO>> getAllBlocks() {
        List<BlockDTO> blocks = blockService.getAllBlocks();
        return new ResponseEntity<>(blocks, HttpStatus.OK);
    }

    @DeleteMapping("/blocks/{id}")
    @ApiOperation(value = "Delete a block by ID")
    public ResponseEntity<Void> deleteBlock(@ApiParam(value = "Block ID") @PathVariable Long id) {
        blockService.deleteBlock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
