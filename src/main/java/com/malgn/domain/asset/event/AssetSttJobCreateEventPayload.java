package com.malgn.domain.asset.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;

@Builder
public record AssetSttJobCreateEventPayload(Long id,
                                            String source)
    implements EventPayload {
}
