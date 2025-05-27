package com.malgn.configure.transcode;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

import com.malgn.configure.transcode.properties.TranscodeProperties;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TranscodeProperties.class)
@Configuration
public class TranscodeConfiguration {

    private static final String FFMPEG_LIB_NAME = "ffmpeg";
    private static final String FFPROBE_LIB_NAME = "ffprobe";


    private final TranscodeProperties properties;


    @Bean
    public FFmpeg ffmpeg() throws IOException {

        Resource lib = properties.lib();
        String libPath = lib.getFile().getAbsolutePath();
        String absolutePath = libPath + File.separatorChar + FFMPEG_LIB_NAME;

        log.info("FFMPEG lib path: {}", absolutePath);

        return new FFmpeg(absolutePath);
    }

    @Bean
    public FFprobe ffprobe() throws IOException {
        Resource lib = properties.lib();
        String libPath = lib.getFile().getAbsolutePath();
        String absolutePath = libPath + File.separatorChar + FFPROBE_LIB_NAME;

        log.info("FFPROVE lib path: {}", absolutePath);

        return new FFprobe(absolutePath);
    }
}
