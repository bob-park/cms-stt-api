package com.malgn.domain.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttTextResponse;

public interface AssetSttTextService {

    Page<AssetSttTextResponse> getAll(Id<AssetSttJob, Long> jobId, Pageable pageable);


}
