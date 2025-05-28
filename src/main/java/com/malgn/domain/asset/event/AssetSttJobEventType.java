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

    CREATE_ASSET_STT_JOB("asset_stt_job", "ASSET_STT_JOB_CREATE", AssetSttJobCreateEventPayload.class),
    ;

    private final String topic;
    private final String type;
    private final Class<? extends EventPayload> payloadClass;

}
