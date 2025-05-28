package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.domain.asset.event.handler.AudioTranscribeHandler;
import com.malgn.domain.asset.event.handler.ExtractAudioHandler;
import com.malgn.domain.asset.event.handler.SpeakerDiarizeHandler;

@RequiredArgsConstructor
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class AppConfiguration {

    private final AppProperties properties;

    private final ExtractAudioHandler extractAudioHandler;
    private final SpeakerDiarizeHandler speakerDiarizeHandler;
    private final AudioTranscribeHandler audioTranscribeHandler;

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {
        DelegatingCommandHandler handler = new DelegatingCommandHandler();

        handler.add(extractAudioHandler);
        handler.add(speakerDiarizeHandler);
        handler.add(audioTranscribeHandler);

        return handler;
    }

}
