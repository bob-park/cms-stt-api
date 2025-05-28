package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "assets_stt_speakers")
public class AssetSttSpeaker extends BaseTimeEntity<Long> {

    @Id
    @SnowflakeIdGenerateValue
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private AssetSttJob job;

    private String speaker;
    private String speakerName;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "speaker")
    private List<AssetSttSpeakerTime> times = new ArrayList<>();

    @Builder
    private AssetSttSpeaker(Long id, String speaker, String speakerName) {

        checkArgument(StringUtils.isNotBlank(speaker), "speaker must not be blank");

        this.id = id;
        this.speaker = speaker;
        this.speakerName = speakerName;
    }

    /*
     * 편의 메서드
     */
    public void updateJob(AssetSttJob job) {
        this.job = job;
    }

    public void addTime(AssetSttSpeakerTime time) {

        time.updateSpeaker(this);

        getTimes().add(time);
    }
}
