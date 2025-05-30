package com.malgn.domain.asset.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.type.task.TaskStatus;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.SelectedSpeakerTextCompletedEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class CompletedAssetSttJobHandler implements CommandHandler<SelectedSpeakerTextCompletedEventPayload> {

    private final OutboxEventPublisher publisher;

    private final AssetSttJobRepository assetSttJobRepository;

    @Transactional
    @Override
    public void handle(Event<SelectedSpeakerTextCompletedEventPayload> event) {

        SelectedSpeakerTextCompletedEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        assetSttJob.updateStatus(TaskStatus.SUCCESS);
    }

    @Override
    public boolean supports(Event<SelectedSpeakerTextCompletedEventPayload> event) {
        return event.getType() == AssetSttJobEventType.SELECTED_SPEAKER_TEXT_COMPLETED;
    }
}
