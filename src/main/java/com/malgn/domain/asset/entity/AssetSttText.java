package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;

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

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.common.entity.annotation.SnowflakeIdGenerateValue;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "assets_stt_texts")
public class AssetSttText extends BaseTimeEntity<Long> {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private AssetSttJob job;

    private BigDecimal startTime;
    private BigDecimal endTime;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id")
    private AssetSttSpeaker speaker;

    private String text;

    @Builder
    private AssetSttText(Long id, BigDecimal startTime, BigDecimal endTime, String text) {

        checkArgument(isNotEmpty(startTime), "startTime must be provided.");
        checkArgument(isNotEmpty(endTime), "endTime must be provided.");
        checkArgument(startTime.compareTo(endTime) <= 0, "startTime must be less than endTime.");

        checkArgument(StringUtils.isNotBlank(text), "text must be provided.");

        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    /*
     * 편의 메서드
     */
    public void updateJob(AssetSttJob job) {
        this.job = job;
    }

    public void updateSpeaker(AssetSttSpeaker speaker) {
        this.speaker = speaker;
    }

}
