package com.malgn.domain.asset.model.v1;

import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.common.type.task.TaskStatus;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttJobResponse;

@Builder
public record AssetSttJobResponseV1(String id,
                                    Long assetId,
                                    Integer numSpeakers,
                                    TaskStatus status,
                                    String sourcePath,
                                    Boolean isDeleted,
                                    LocalDateTime createdDate,
                                    LocalDateTime lastModifiedDate)
    implements AssetSttJobResponse {

    public static AssetSttJobResponse from(AssetSttJob entity) {
        return AssetSttJobResponseV1.builder()
            .id(String.valueOf(entity.getId()))
            .assetId(entity.getAssetId())
            .numSpeakers(entity.getNumSpeakers())
            .status(entity.getStatus())
            .sourcePath(entity.getSourcePath())
            .isDeleted(entity.getIsDeleted())
            .createdDate(entity.getCreatedDate())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }
}
