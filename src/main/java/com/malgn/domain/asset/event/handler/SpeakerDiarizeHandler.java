package com.malgn.domain.asset.event.handler;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.event.ExtractAudioCompletedEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.audio.feign.SpeakerDiarizeFeignClient;
import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;
import com.malgn.domain.audio.model.CommonMultipartFile;

@Slf4j
public class SpeakerDiarizeHandler implements CommandHandler<ExtractAudioCompletedEventPayload> {

    private final AppProperties properties;

    private final SpeakerDiarizeFeignClient speakerDiarizeClient;

    private final OutboxEventPublisher publisher;
    private final AssetSttJobRepository assetSttJobRepository;

    @Builder
    private SpeakerDiarizeHandler(AppProperties properties, SpeakerDiarizeFeignClient speakerDiarizeClient,
        OutboxEventPublisher publisher, AssetSttJobRepository assetSttJobRepository) {

        checkArgument(isNotEmpty(properties), "properties must provided.");
        checkArgument(isNotEmpty(speakerDiarizeClient), "speakerDiarizeClient must provided.");
        checkArgument(isNotEmpty(publisher), "publisher must provided.");
        checkArgument(isNotEmpty(assetSttJobRepository), "assetSttJobRepository must provided.");

        this.properties = properties;
        this.speakerDiarizeClient = speakerDiarizeClient;
        this.publisher = publisher;
        this.assetSttJobRepository = assetSttJobRepository;
    }

    @Override
    public void handle(Event<ExtractAudioCompletedEventPayload> event) {

        ExtractAudioCompletedEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        String dir = null;
        List<AudioSpeakerDiarizationResponse> result = null;

        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();

            String source = dir + File.separatorChar + assetSttJob.getAudioPath();

            CommonMultipartFile audioFile = new CommonMultipartFile(IOUtils.toByteArray(new FileInputStream(source)));

            result = speakerDiarizeClient.diarize(audioFile);

        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

    }

    @Override
    public boolean supports(Event<ExtractAudioCompletedEventPayload> event) {
        return CommandHandler.super.supports(event);
    }
}
