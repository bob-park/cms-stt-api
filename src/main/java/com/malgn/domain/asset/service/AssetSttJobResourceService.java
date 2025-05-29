package com.malgn.domain.asset.service;

import java.io.IOException;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpRange;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;

public interface AssetSttJobResourceService {

    ResourceRegion getResourceWithRange(HttpRange range, Id<AssetSttJob, Long> jobId) throws IOException;

}
