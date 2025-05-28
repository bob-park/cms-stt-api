package com.malgn.domain.asset.event.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AudioTranscribeCompletedEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.repository.AssetSttSpeakerRepository;
import com.malgn.domain.asset.repository.AssetSttSpeakerTimeRepository;
import com.malgn.domain.audio.feign.SpeakerDiarizeFeignClient;
import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;
import com.malgn.domain.audio.model.CommonMultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpeakerDiarizeHandler implements CommandHandler<AudioTranscribeCompletedEventPayload> {

    private final AppProperties properties;

    private final OutboxEventPublisher publisher;

    private final SpeakerDiarizeFeignClient speakerDiarizeClient;

    private final AssetSttJobRepository assetSttJobRepository;
    private final AssetSttSpeakerRepository assetSttSpeakerRepository;
    private final AssetSttSpeakerTimeRepository assetSttSpeakerTimeRepository;

    @Override
    public void handle(Event<AudioTranscribeCompletedEventPayload> event) {

        AudioTranscribeCompletedEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        String dir = null;
        List<AudioSpeakerDiarizationResponse> result = List.of();

        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();

            String source = dir + File.separatorChar + assetSttJob.getAudioPath();

            CommonMultipartFile audioFile = new CommonMultipartFile(IOUtils.toByteArray(new FileInputStream(source)));

            result = speakerDiarizeClient.diarize(audioFile);

        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        // create speakers
        Set<String> speakerIds =
            result.stream()
                .map(AudioSpeakerDiarizationResponse::speaker)
                .collect(Collectors.toSet());

        List<AssetSttSpeaker> speakers = Lists.newArrayList();

        for (String speakerId : speakerIds) {
            AssetSttSpeaker createdSpeaker =
                AssetSttSpeaker.builder()
                    .speaker(speakerId)
                    .build();

            assetSttJob.addSpeaker(createdSpeaker);

            createdSpeaker = assetSttSpeakerRepository.save(createdSpeaker);

            log.debug("created speaker. ({})", createdSpeaker);

            speakers.add(createdSpeaker);

        }

        // create speaker times
        for (AudioSpeakerDiarizationResponse item : result) {

            AssetSttSpeaker speaker = findSpeaker(item.speaker(), speakers);

            if (speaker == null) {
                log.warn("speaker not found. ({})", item.speaker());
                continue;
            }

            AssetSttSpeakerTime createdTime =
                AssetSttSpeakerTime.builder()
                    .startTime(item.start().doubleValue())
                    .endTime(item.end().doubleValue())
                    .build();

            speaker.addTime(createdTime);

            createdTime = assetSttSpeakerTimeRepository.save(createdTime);

            log.debug("created speaker time. ({})", createdTime);

        }

    }

    @Override
    public boolean supports(Event<AudioTranscribeCompletedEventPayload> event) {
        return event.getType() == AssetSttJobEventType.AUDIO_TRANSCRIBE_COMPLETED;
    }

    private AssetSttSpeaker findSpeaker(String speakerId, List<AssetSttSpeaker> speakers) {
        return speakers.stream()
            .filter(speaker -> StringUtils.equals(speaker.getSpeaker(), speakerId))
            .findAny()
            .orElse(null);
    }
}
