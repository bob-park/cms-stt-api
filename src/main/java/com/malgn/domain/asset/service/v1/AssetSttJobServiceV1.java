package com.malgn.domain.asset.service.v1;

import static com.malgn.domain.asset.model.v1.AssetSttJobResponseV1.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.event.AssetSttJobCreateEventPayload;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.model.AssetSttJobResponse;
import com.malgn.domain.asset.model.CreateAssetSttJobRequest;
import com.malgn.domain.asset.model.v1.AssetSttJobResponseV1;
import com.malgn.domain.asset.model.v1.CreateAssetSttJobRequestV1;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.service.AssetSttJobService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AssetSttJobServiceV1 implements AssetSttJobService {

    private final OutboxEventPublisher publisher;

    private final AssetSttJobRepository assetSttJobRepository;

    @Transactional
    @Override
    public AssetSttJobResponse createJob(long assetId, CreateAssetSttJobRequest createRequest) {

        CreateAssetSttJobRequestV1 v1Request = (CreateAssetSttJobRequestV1)createRequest;

        AssetSttJob createdJob =
            AssetSttJob.builder()
                .assetId(assetId)
                .sourcePath(v1Request.sourcePath())
                .build();

        createdJob = assetSttJobRepository.save(createdJob);

        log.debug("created job. ({})", createdJob);

        publisher.publish(
            AssetSttJobEventType.CREATE_ASSET_STT_JOB,
            AssetSttJobCreateEventPayload.builder()
                .id(createdJob.getId())
                .source(createdJob.getSourcePath())
                .build());

        return from(createdJob);
    }
}
