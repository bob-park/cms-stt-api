package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "assets_stt_audios")
public class AssetSttAudio extends BaseTimeEntity<Long> {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private AssetSttJob job;

    @Enumerated(EnumType.STRING)
    private AssetSttAudioType type;

    private Long fileIndex;

    private BigDecimal startTime;
    private BigDecimal endTime;

    private String audioPath;

    @Builder
    private AssetSttAudio(Long id, AssetSttAudioType type, Long fileIndex, BigDecimal startTime, BigDecimal endTime,
        String audioPath) {

        checkArgument(isNotEmpty(type), "type must be provided.");
        checkArgument(isNotEmpty(fileIndex), "fileIndex must be provided.");
        checkArgument(isNotEmpty(startTime), "startTime must be provided.");
        checkArgument(isNotEmpty(endTime), "endTime must be provided.");
        checkArgument(StringUtils.isNotBlank(audioPath), "audioPath must be provided.");

        this.id = id;
        this.type = type;
        this.fileIndex = fileIndex;
        this.startTime = startTime;
        this.endTime = endTime;
        this.audioPath = audioPath;
    }

    /*
     * 편의 메서드
     */
    public void updateJob(AssetSttJob job) {
        this.job = job;
    }
}
