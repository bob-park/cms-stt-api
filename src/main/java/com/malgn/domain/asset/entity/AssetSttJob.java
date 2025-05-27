package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;
import com.malgn.common.type.task.TaskStatus;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "assets_stt_jobs")
public class AssetSttJob extends BaseTimeEntity<Long> {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    private Long assetId;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private String sourcePath;
    private String audioPath;

    private Boolean isDeleted;

    @Builder
    private AssetSttJob(Long id, Long assetId, TaskStatus status, String sourcePath, String audioPath,
        Boolean isDeleted) {

        checkArgument(isNotEmpty(assetId), "assetId must be provided.");
        checkArgument(StringUtils.isNotBlank(sourcePath), "assetId must be provided.");

        this.id = id;
        this.assetId = assetId;
        this.status = defaultIfNull(status, TaskStatus.WAITING);
        this.sourcePath = sourcePath;
        this.audioPath = audioPath;
        this.isDeleted = defaultIfNull(isDeleted, false);
    }
}
