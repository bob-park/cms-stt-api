package com.malgn.domain.asset.repository.query;

import java.util.List;

import com.malgn.domain.asset.entity.AssetSttSpeaker;

public interface AssetSttSpeakerQueryRepository {

    List<AssetSttSpeaker> getAll(long jobId);

}
