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
import com.malgn.domain.asset.entity.AssetSttAudio;
import com.malgn.domain.asset.entity.AssetSttAudioType;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AudioTranscribeCompletedEventPayload;
import com.malgn.domain.asset.event.ExtractAudioCompletedEventPayload;
import com.malgn.domain.asset.event.SpeakerDiarizeCompleteEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.repository.AssetSttSpeakerRepository;
import com.malgn.domain.asset.repository.AssetSttSpeakerTimeRepository;
import com.malgn.domain.audio.feign.SpeakerDiarizeFeignClient;
import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;
import com.malgn.domain.audio.model.CommonMultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class SpeakerDiarizeHandler implements CommandHandler<ExtractAudioCompletedEventPayload> {

    private final AppProperties properties;

    private final OutboxEventPublisher publisher;

    private final SpeakerDiarizeFeignClient speakerDiarizeClient;

    private final AssetSttJobRepository assetSttJobRepository;
    private final AssetSttSpeakerRepository assetSttSpeakerRepository;
    private final AssetSttSpeakerTimeRepository assetSttSpeakerTimeRepository;

    @Override
    public void handle(Event<ExtractAudioCompletedEventPayload> event) {

        ExtractAudioCompletedEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        String dir = null;

        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        AssetSttAudio audio =
            assetSttJob.getAudios().stream()
                .filter(item -> item.getType() == AssetSttAudioType.ALL)
                .findAny()
                .orElseThrow(() -> new NotFoundException("No exist all audio."));

        String absoluteAudioPath = dir + File.separatorChar + audio.getAudioPath();

        List<AudioSpeakerDiarizationResponse> result = List.of();

        try {
            CommonMultipartFile audioFile =
                new CommonMultipartFile(IOUtils.toByteArray(new FileInputStream(absoluteAudioPath)));

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
                    .startTime(item.start())
                    .endTime(item.end())
                    .build();

            speaker.addTime(createdTime);

            createdTime = assetSttSpeakerTimeRepository.save(createdTime);

            log.debug("created speaker time. ({})", createdTime);

        }

        publisher.publish(
            AssetSttJobEventType.SPEAKER_DIARIZE_COMPLETED,
            SpeakerDiarizeCompleteEventPayload.builder()
                .id(assetSttJob.getId())
                .build());

    }

    @Override
    public boolean supports(Event<ExtractAudioCompletedEventPayload> event) {
        return event.getType() == AssetSttJobEventType.EXTRACT_AUDIO_COMPLETED;
    }

    private AssetSttSpeaker findSpeaker(String speakerId, List<AssetSttSpeaker> speakers) {
        return speakers.stream()
            .filter(speaker -> StringUtils.equals(speaker.getSpeaker(), speakerId))
            .findAny()
            .orElse(null);
    }
}
