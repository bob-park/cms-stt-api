package com.malgn.domain.transcode.runner.extract;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.bramp.ffmpeg.FFmpeg;

class ExtractAudioRunnerTest {

    FFmpeg ffmpeg;

    @BeforeEach
    void setup() throws IOException {

        String lib = "/Users/hwpark/Documents/ffmpeg/ffmpeg";

        this.ffmpeg = new FFmpeg(lib);
    }

    @Test
    void run() {

        String source = "/Users/hwpark/Documents/docker-workspace/speaker-diarize/storage/2025/05/27/test.mp4";
        String dest = "/Users/hwpark/Documents/docker-workspace/speaker-diarize/storage/2025/05/27/test.wav";

        ExtractAudioRunner runner = new ExtractAudioRunner(ffmpeg);

        runner.run(
            ExtractAudioRequest.builder()
                .source(source)
                .dest(dest)
                .build());

    }
}
