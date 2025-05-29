package com.malgn.domain.asset.service.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.model.AssetSttSpeakerResponse;
import com.malgn.domain.asset.model.v1.AssetSttSpeakerResponseV1;
import com.malgn.domain.asset.repository.AssetSttSpeakerRepository;
import com.malgn.domain.asset.service.AssetSttSpeakerService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AssetSttSpeakerServiceV1 implements AssetSttSpeakerService {

    private final AssetSttSpeakerRepository assetSttSpeakerRepository;

    @Override
    public List<AssetSttSpeakerResponse> getAll(Id<AssetSttJob, Long> jobId) {

        List<AssetSttSpeaker> result = assetSttSpeakerRepository.getAll(jobId.getValue());

        return result.stream()
            .map(AssetSttSpeakerResponseV1::from)
            .toList();
    }
}
