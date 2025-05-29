package com.malgn.domain.asset.repository.query;

import java.util.List;

import com.malgn.domain.asset.entity.AssetSttText;

public interface AssetSttTextQueryRepository {

    List<AssetSttText> getAll(Long jobId);

}
