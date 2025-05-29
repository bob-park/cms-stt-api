package com.malgn.domain.asset.service;

import java.util.List;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttSpeakerResponse;

public interface AssetSttSpeakerService {

    List<AssetSttSpeakerResponse> getAll(Id<AssetSttJob, Long> jobId);

}
