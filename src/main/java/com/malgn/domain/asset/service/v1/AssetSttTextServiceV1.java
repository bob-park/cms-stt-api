package com.malgn.domain.asset.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.model.AssetSttTextResponse;
import com.malgn.domain.asset.model.v1.AssetSttTextResponseV1;
import com.malgn.domain.asset.repository.AssetSttTextRepository;
import com.malgn.domain.asset.service.AssetSttTextService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AssetSttTextServiceV1 implements AssetSttTextService {

    private final AssetSttTextRepository assetSttTextRepository;

    @Override
    public Page<AssetSttTextResponse> getAll(Id<AssetSttJob, Long> jobId, Pageable pageable) {

        Page<AssetSttText> result = assetSttTextRepository.getAll(jobId.getValue(), pageable);

        return result.map(AssetSttTextResponseV1::from);
    }
}
