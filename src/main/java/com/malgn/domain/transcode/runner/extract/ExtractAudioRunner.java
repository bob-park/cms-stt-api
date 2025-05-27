package com.malgn.domain.transcode.runner.extract;

import static com.google.common.base.Preconditions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.domain.transcode.runner.TranscodeRequest;
import com.malgn.domain.transcode.runner.TranscodeRunner;
import com.malgn.domain.transcode.runner.TranscodeType;

@Slf4j
@RequiredArgsConstructor
public class ExtractAudioRunner implements TranscodeRunner {

    // codec
    private static final String AUDIO_CODEC = "pcm_s16le";

    // audio channel count
    private static final int AUDIO_CHANNELS = 1;

    // audio sample rate
    private static final long AUDIO_SAMPLE_RATE = 16_000;

    // extension
    public static final String AUDIO_EXTENSION = ".wav";

    private final FFmpeg ffmpeg;

    @Override
    public void run(TranscodeRequest request) {

        ExtractAudioRequest extractRequest = (ExtractAudioRequest)request;

        checkArgument(StringUtils.isNotBlank(extractRequest.source()), "source must be provided.");
        checkArgument(StringUtils.isNotBlank(extractRequest.dest()), "dest must be provided.");

        if (!Files.exists(Paths.get(extractRequest.source()))) {
            throw new NotFoundException(String.format("Source %s not found.", extractRequest.source()));
        }

        try {
            FileUtils.forceMkdirParent(new File(request.dest()));
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        FFmpegBuilder builder =
            new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(extractRequest.source())
                .addOutput(request.dest())
                .addExtraArgs("-c:a", AUDIO_CODEC)
                .addExtraArgs("-ac", AUDIO_CHANNELS + "")
                .addExtraArgs("-ar", AUDIO_SAMPLE_RATE + "")
                .done();

        try {
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

            executor.createJob(builder).run();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

    }

    @Override
    public boolean supports(TranscodeType type) {
        return type == TranscodeType.EXTRACT_AUDIO;
    }
}
