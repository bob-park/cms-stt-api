package com.malgn.domain.asset.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;

@Builder
public record ExtractAudioCompletedEventPayload(Long id,
                                                String audioPath)
    implements EventPayload {
}
