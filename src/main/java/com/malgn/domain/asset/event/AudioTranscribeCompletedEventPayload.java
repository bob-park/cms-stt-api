package com.malgn.domain.asset.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;

@Builder
public record AudioTranscribeCompletedEventPayload(Long id,
                                                   String audioPath)
    implements EventPayload {
}
