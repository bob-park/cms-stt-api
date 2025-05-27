package com.malgn.domain.asset.service;

import com.malgn.domain.asset.model.AssetSttJobResponse;
import com.malgn.domain.asset.model.CreateAssetSttJobRequest;

public interface AssetSttJobService {

    AssetSttJobResponse createJob(long assetId, CreateAssetSttJobRequest createRequest);

}
