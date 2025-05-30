package com.malgn.domain.asset.event.handler;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AudioTranscribeCompletedEventPayload;
import com.malgn.domain.asset.event.SelectedSpeakerTextCompletedEventPayload;
import com.malgn.domain.asset.repository.AssetSttSpeakerTimeRepository;
import com.malgn.domain.asset.repository.AssetSttTextRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class SelectedSpeakerTextHandler implements CommandHandler<AudioTranscribeCompletedEventPayload> {

    private final OutboxEventPublisher publisher;

    private final AssetSttTextRepository assetSttTextRepository;
    private final AssetSttSpeakerTimeRepository assetSttSpeakerTimeRepository;

    @Transactional
    @Override
    public void handle(Event<AudioTranscribeCompletedEventPayload> event) {

        AudioTranscribeCompletedEventPayload payload = event.getPayload();

        List<AssetSttText> texts = assetSttTextRepository.getAll(payload.id());

        for (AssetSttText text : texts) {

            AssetSttSpeakerTime speakerTime =
                assetSttSpeakerTimeRepository.getSpeakerTime(payload.id(), text.getStartTime(), text.getEndTime());

            if (speakerTime == null) {
                continue;
            }

            speakerTime.getSpeaker().addText(text);

            log.debug("updated asset stt speaker text. ({})", text);

        }

        publisher.publish(
            AssetSttJobEventType.SELECTED_SPEAKER_TEXT_COMPLETED,
            SelectedSpeakerTextCompletedEventPayload.builder()
                .id(payload.id())
                .build()
        );

    }

    @Override
    public boolean supports(Event<AudioTranscribeCompletedEventPayload> event) {
        return event.getType() == AssetSttJobEventType.AUDIO_TRANSCRIBE_COMPLETED;
    }
}
