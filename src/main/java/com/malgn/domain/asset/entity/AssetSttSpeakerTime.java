package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import org.apache.commons.lang3.ObjectUtils;

import com.google.common.base.Preconditions;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "assets_stt_speakers_times")
public class AssetSttSpeakerTime extends BaseTimeEntity<Long> {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id")
    private AssetSttSpeaker speaker;

    @Column(columnDefinition = "real")
    private Double startTime;

    @Column(columnDefinition = "real")
    private Double endTime;

    @Builder
    private AssetSttSpeakerTime(Long id, Double startTime, Double endTime) {

        checkArgument(isNotEmpty(startTime), "startTime must provided.");
        checkArgument(isNotEmpty(endTime), "endTime must provided.");

        checkArgument(startTime <= endTime, "startTime must be less than endTime.");

        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /*
     * 편의 메서드
     */
    public void updateSpeaker(AssetSttSpeaker speaker) {
        this.speaker = speaker;
    }
}
