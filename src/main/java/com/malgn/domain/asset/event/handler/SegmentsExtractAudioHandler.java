package com.malgn.domain.asset.event.handler;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.common.type.task.TaskStatus;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttAudio;
import com.malgn.domain.asset.entity.AssetSttAudioType;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.event.AssetSttJobCreatedEventPayload;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.ExtractAudioCompletedEventPayload;
import com.malgn.domain.asset.event.SpeakerDiarizeCompleteEventPayload;
import com.malgn.domain.asset.repository.AssetSttAudioRepository;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRequest;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@Slf4j
@RequiredArgsConstructor
@Component
public class SegmentsExtractAudioHandler implements CommandHandler<SpeakerDiarizeCompleteEventPayload> {

    private static final int MAX_AUDIO_SECONDS = 30;

    private final AppProperties properties;

    private final FFprobe ffprobe;

    private final ExtractAudioRunner runner;
    private final OutboxEventPublisher publisher;

    private final AssetSttJobRepository assetSttJobRepository;
    private final AssetSttAudioRepository assetSttAudioRepository;

    @Transactional
    @Override
    public void handle(Event<SpeakerDiarizeCompleteEventPayload> event) {

        SpeakerDiarizeCompleteEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        assetSttJob.updateStatus(TaskStatus.PROCEEDING);

        // duration
        BigDecimal duration = getDuration(assetSttJob);

        // audio 분할
        segmentAudio(assetSttJob, duration);

        publisher.publish(
            AssetSttJobEventType.SEGMENTS_EXTRACT_AUDIO_COMPLETED,
            ExtractAudioCompletedEventPayload.builder()
                .id(assetSttJob.getId())
                .build());
    }

    @Override
    public boolean supports(Event<SpeakerDiarizeCompleteEventPayload> event) {
        return event.getType() == AssetSttJobEventType.SPEAKER_DIARIZE_COMPLETED;
    }

    private String getAbsoluteDirPath() {
        try {
            return properties.baseLocation().getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }
    }

    private BigDecimal getDuration(AssetSttJob assetSttJob) {
        String dir = getAbsoluteDirPath();
        String source = dir + File.separatorChar + assetSttJob.getSourcePath();

        BigDecimal duration = BigDecimal.ZERO;

        try {
            FFmpegProbeResult result = ffprobe.probe(source);

            for (FFmpegStream stream : result.streams) {
                if (StringUtils.equalsIgnoreCase("VIDEO", stream.codec_type.name())) {
                    duration = BigDecimal.valueOf(stream.duration);
                    log.debug("stream.duration={}", stream.duration);
                }
            }

        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        return duration;
    }

    private void segmentAudio(AssetSttJob assetSttJob, BigDecimal duration) {
        String dir = getAbsoluteDirPath();

        String baseName = FilenameUtils.getBaseName(assetSttJob.getSourcePath());
        String source = dir + File.separatorChar + assetSttJob.getSourcePath();

        // audio file 분할
        int totalFileCount = (int)(duration.doubleValue() / MAX_AUDIO_SECONDS);

        if (duration.doubleValue() % MAX_AUDIO_SECONDS > 0) {
            totalFileCount++;
        }

        for (int i = 0; i < totalFileCount; i++) {
            String relativeDestPath =
                FilenameUtils.getFullPath(assetSttJob.getSourcePath())
                    + baseName + File.separatorChar
                    + baseName + "_" + i + "." + ExtractAudioRunner.AUDIO_EXTENSION;

            double startTime = (double)MAX_AUDIO_SECONDS * i;
            double sectionDuration = Math.min(duration.doubleValue() - startTime, MAX_AUDIO_SECONDS);

            runner.run(
                ExtractAudioRequest.builder()
                    .source(source)
                    .dest(dir + File.separatorChar + relativeDestPath)
                    .startSeconds(startTime)
                    .duration(sectionDuration)
                    .build());

            AssetSttAudio createdAudio =
                AssetSttAudio.builder()
                    .type(AssetSttAudioType.SEGMENT)
                    .fileIndex((long)i)
                    .startTime(BigDecimal.valueOf(startTime))
                    .endTime(BigDecimal.valueOf(startTime + sectionDuration))
                    .audioPath(relativeDestPath)
                    .build();

            assetSttJob.addAudio(createdAudio);

            createdAudio = assetSttAudioRepository.save(createdAudio);

            log.debug("created audio. ({})", createdAudio);

        }
    }
}
