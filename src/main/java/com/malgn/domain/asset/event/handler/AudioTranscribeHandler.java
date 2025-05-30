package com.malgn.domain.asset.event.handler;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttAudio;
import com.malgn.domain.asset.entity.AssetSttAudioType;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AudioTranscribeCompletedEventPayload;
import com.malgn.domain.asset.event.SpeakerDiarizeCompleteEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.repository.AssetSttSpeakerTimeRepository;
import com.malgn.domain.asset.repository.AssetSttTextRepository;
import com.malgn.domain.audio.client.OpenAiAudioTranscriptionClient;
import com.malgn.domain.audio.model.ai.OpenAiAudioTranscriptionResponse;
import com.malgn.domain.audio.model.ai.OpenAiAudioTranscriptionSegment;

@Slf4j
@RequiredArgsConstructor
@Component
public class AudioTranscribeHandler implements CommandHandler<SpeakerDiarizeCompleteEventPayload> {

    private static final String DEFAULT_PROMPT = """
        대화의 한문장씩 추출해줘
        """;

    private final AppProperties properties;

    private final OutboxEventPublisher publisher;

    private final OpenAiAudioTranscriptionClient transcribeClient;

    private final AssetSttJobRepository assetSttJobRepository;
    private final AssetSttTextRepository assetSttTextRepository;
    private final AssetSttSpeakerTimeRepository assetSttSpeakerTimeRepository;

    @Override
    public void handle(Event<SpeakerDiarizeCompleteEventPayload> event) {

        SpeakerDiarizeCompleteEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        String dir = null;
        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        List<AssetSttAudio> audios =
            assetSttJob.getAudios().stream()
                .filter(item -> item.getType() == AssetSttAudioType.SEGMENT)
                .toList();

        for (AssetSttAudio audio : audios) {
            String absoluteAudioPath = dir + File.separatorChar + audio.getAudioPath();

            OpenAiAudioTranscriptionResponse result =
                transcribeClient.transcribe(new FileSystemResource(absoluteAudioPath));

            List<OpenAiAudioTranscriptionSegment> segments = result.segments();

            for (OpenAiAudioTranscriptionSegment segment : segments) {

                BigDecimal startTime = segment.start().add(audio.getStartTime());
                BigDecimal endTime = segment.end().add(audio.getStartTime());

                AssetSttText createdText =
                    AssetSttText.builder()
                        .startTime(startTime)
                        .endTime(endTime)
                        .text(segment.text())
                        .build();

                assetSttJob.addText(createdText);

                createdText = assetSttTextRepository.save(createdText);

                log.debug("created asset stt text. ({})", createdText);

            }

        }

        publisher.publish(
            AssetSttJobEventType.AUDIO_TRANSCRIBE_COMPLETED,
            AudioTranscribeCompletedEventPayload.builder()
                .id(assetSttJob.getId())
                .build());

    }

    @Override
    public boolean supports(Event<SpeakerDiarizeCompleteEventPayload> event) {
        return event.getType() == AssetSttJobEventType.SPEAKER_DIARIZE_COMPLETED;
    }
}
