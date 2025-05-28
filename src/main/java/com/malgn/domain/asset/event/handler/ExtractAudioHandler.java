package com.malgn.domain.asset.event.handler;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.common.type.task.TaskStatus;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.event.AssetSttJobCreatedEventPayload;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.ExtractAudioCompletedEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRequest;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@Slf4j
@RequiredArgsConstructor
public class ExtractAudioHandler implements CommandHandler<AssetSttJobCreatedEventPayload> {

    private final AppProperties properties;

    private final ExtractAudioRunner runner;
    private final OutboxEventPublisher publisher;

    private final AssetSttJobRepository assetSttJobRepository;

    @Override
    public void handle(Event<AssetSttJobCreatedEventPayload> event) {

        AssetSttJobCreatedEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        assetSttJob.updateStatus(TaskStatus.PROCEEDING);

        String dir = null;

        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        String relativeDest =
            FilenameUtils.getFullPath(assetSttJob.getSourcePath())
                + FilenameUtils.getBaseName(assetSttJob.getSourcePath()) + "." + ExtractAudioRunner.AUDIO_EXTENSION;

        String source = dir + File.separatorChar + assetSttJob.getSourcePath();
        String dest = dir + File.separatorChar + relativeDest;

        runner.run(
            ExtractAudioRequest.builder()
                .source(source)
                .dest(dest)
                .build());

        assetSttJob.updateAudioPath(relativeDest);

        publisher.publish(
            AssetSttJobEventType.EXTRACT_AUDIO_COMPLETED,
            ExtractAudioCompletedEventPayload.builder()
                .id(assetSttJob.getId())
                .audioPath(assetSttJob.getAudioPath())
                .build());
    }

    @Override
    public boolean supports(Event<AssetSttJobCreatedEventPayload> event) {
        return event.getType() == AssetSttJobEventType.ASSET_STT_JOB_CREATED;
    }
}
