package com.malgn.domain.asset.model.v1;

import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.model.AssetSttSpeakerResponse;

@Builder
public record AssetSttSpeakerResponseV1(String id,
                                        String speaker,
                                        String speakerName,
                                        LocalDateTime createdDate,
                                        LocalDateTime lastModifiedDate)
    implements AssetSttSpeakerResponse {

    public static AssetSttSpeakerResponse from(AssetSttSpeaker speaker) {
        return AssetSttSpeakerResponseV1.builder()
            .id(String.valueOf(speaker.getId()))
            .speaker(speaker.getSpeaker())
            .speakerName(speaker.getSpeakerName())
            .createdDate(speaker.getCreatedDate())
            .lastModifiedDate(speaker.getLastModifiedDate())
            .build();
    }
}
