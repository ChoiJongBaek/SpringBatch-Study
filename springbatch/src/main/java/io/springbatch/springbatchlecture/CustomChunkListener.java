package io.springbatch.springbatchlecture;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

public class CustomChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println(">> before Chunk");
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println(">> after Chunk");
    }

    @AfterChunkError
    public void afterChunkError(ChunkContext chunkContext) {
        System.out.println(">> after Chunk Error");
    }
}
