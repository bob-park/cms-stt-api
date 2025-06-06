package com.malgn.domain.asset.model.v1;

import com.malgn.domain.asset.model.CreateAssetSttJobRequest;

public record CreateAssetSttJobRequestV1(String sourcePath,
                                         Integer numSpeakers)
    implements CreateAssetSttJobRequest {
}
