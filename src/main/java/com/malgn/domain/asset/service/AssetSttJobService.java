package com.malgn.domain.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.asset.model.AssetSttJobResponse;
import com.malgn.domain.asset.model.CreateAssetSttJobRequest;

public interface AssetSttJobService {

    AssetSttJobResponse createJob(long assetId, CreateAssetSttJobRequest createRequest);

    Page<AssetSttJobResponse> getAll(long assetId, Pageable pageable);

    AssetSttJobResponse getJob(long assetId, long jobId);

}
