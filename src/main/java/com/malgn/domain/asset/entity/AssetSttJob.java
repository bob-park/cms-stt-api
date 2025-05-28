package com.malgn.domain.asset.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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

    private Boolean isDeleted;

    @Exclude
    @OrderBy("startTime")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    private List<AssetSttAudio> audios = new ArrayList<>();

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    private List<AssetSttSpeaker> speakers = new ArrayList<>();

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    private List<AssetSttText> texts = new ArrayList<>();

    @Builder
    private AssetSttJob(Long id, Long assetId, TaskStatus status, String sourcePath, Boolean isDeleted) {

        checkArgument(isNotEmpty(assetId), "assetId must be provided.");
        checkArgument(StringUtils.isNotBlank(sourcePath), "assetId must be provided.");

        this.id = id;
        this.assetId = assetId;
        this.status = defaultIfNull(status, TaskStatus.WAITING);
        this.sourcePath = sourcePath;
        this.isDeleted = defaultIfNull(isDeleted, false);
    }

    /*
     * 편의 메서드
     */
    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public void addAudio(AssetSttAudio audio) {

        audio.updateJob(this);

        getAudios().add(audio);
    }

    public void addSpeaker(AssetSttSpeaker speaker) {

        speaker.updateJob(this);

        getSpeakers().add(speaker);
    }

    public void addText(AssetSttText text) {
        text.updateJob(this);

        getTexts().add(text);
    }

}
