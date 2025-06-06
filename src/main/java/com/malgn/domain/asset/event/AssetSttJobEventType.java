package com.malgn.domain.asset.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import groovy.transform.ToString;

import com.malgn.cqrs.event.EventPayload;
import com.malgn.cqrs.event.EventType;

@ToString
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum AssetSttJobEventType implements EventType {

    ASSET_STT_JOB_CREATED(Topic.ASSET_STT_JOB, "ASSET_STT_JOB_CREATED", AssetSttJobCreatedEventPayload.class),
    EXTRACT_AUDIO_COMPLETED(Topic.ASSET_STT_JOB, "EXTRACT_AUDIO_COMPLETED", ExtractAudioCompletedEventPayload.class),
    SPEAKER_DIARIZE_COMPLETED(Topic.ASSET_STT_JOB, "SPEAKER_DIARIZE_COMPLETED", SpeakerDiarizeCompleteEventPayload.class),
    SEGMENTS_EXTRACT_AUDIO_COMPLETED(Topic.ASSET_STT_JOB, "SEGMENTS_EXTRACT_AUDIO_COMPLETED", SegmentsExtractAudioCompletedEventPayload.class),
    AUDIO_TRANSCRIBE_COMPLETED(Topic.ASSET_STT_JOB, "AUDIO_TRANSCRIBE_COMPLETED", AudioTranscribeCompletedEventPayload.class),
    SELECTED_SPEAKER_TEXT_COMPLETED(Topic.ASSET_STT_JOB, "SELECTED_SPEAKER_TEXT_COMPLETED", SelectedSpeakerTextCompletedEventPayload.class)    ;

    private final String topic;
    private final String type;
    private final Class<? extends EventPayload> payloadClass;

    public static class Topic {
        public static final String ASSET_STT_JOB = "asset_stt_job";
    }

}
