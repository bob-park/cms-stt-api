package com.malgn.domain.asset.model.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.model.AssetSttSpeakerResponse;
import com.malgn.domain.asset.model.AssetSttTextResponse;

@Builder
public record AssetSttTextResponseV1(String id,
                                     BigDecimal startTime,
                                     BigDecimal endTime,
                                     AssetSttSpeakerResponse speaker,
                                     String text,
                                     LocalDateTime createdDate,
                                     LocalDateTime lastModifiedDate)
    implements AssetSttTextResponse {

    public static AssetSttTextResponse from(AssetSttText text) {
        return AssetSttTextResponseV1.builder()
            .id(String.valueOf(text.getId()))
            .startTime(text.getStartTime())
            .endTime(text.getEndTime())
            .speaker(text.getSpeaker() != null ? AssetSttSpeakerResponseV1.from(text.getSpeaker()) : null)
            .text(text.getText())
            .createdDate(text.getCreatedDate())
            .lastModifiedDate(text.getLastModifiedDate())
            .build();
    }
}
