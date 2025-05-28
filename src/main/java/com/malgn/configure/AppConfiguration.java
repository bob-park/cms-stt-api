package com.malgn.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.handler.DelegatingCommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.event.handler.ExtractAudioHandler;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@RequiredArgsConstructor
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class AppConfiguration {

    private final AppProperties properties;

    private final AssetSttJobRepository assetSttJobRepository;

    private final ExtractAudioRunner extractAudioRunner;

    private final OutboxEventPublisher publisher;

    @Bean
    public DelegatingCommandHandler delegatingCommandHandler() {
        DelegatingCommandHandler handler = new DelegatingCommandHandler();

        handler.add(new ExtractAudioHandler(properties, extractAudioRunner, publisher, assetSttJobRepository));

        return handler;
    }

}
