package com.malgn.domain.asset.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;

@Builder
public record SelectedSpeakerTextCompletedEventPayload(Long id)
    implements EventPayload {
}
