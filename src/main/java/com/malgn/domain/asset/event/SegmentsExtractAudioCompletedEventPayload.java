package com.malgn.domain.asset.event;

import com.malgn.cqrs.event.EventPayload;

public record SegmentsExtractAudioCompletedEventPayload(Long id)
    implements EventPayload {
}
