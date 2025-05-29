package com.malgn.domain.asset.controller.v1;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttJobResponse;
import com.malgn.domain.asset.model.v1.CreateAssetSttJobRequestV1;
import com.malgn.domain.asset.service.v1.AssetSttJobResourceServiceV1;
import com.malgn.domain.asset.service.v1.AssetSttJobServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/assets/{assetId:\\d+}/stt/jobs")
public class AssetSttJobControllerV1 {

    private final AssetSttJobServiceV1 assetSttJobService;
    private final AssetSttJobResourceServiceV1 assetSttJobResourceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public AssetSttJobResponse createJob(@PathVariable long assetId,
        @RequestBody CreateAssetSttJobRequestV1 createRequest) {
        return assetSttJobService.createJob(assetId, createRequest);
    }

    @GetMapping(path = "")
    public Page<AssetSttJobResponse> getJobs(@PathVariable long assetId,
        @PageableDefault(size = 25, sort = "createdDate", direction = Direction.DESC) Pageable pageable) {
        return assetSttJobService.getAll(assetId, pageable);
    }

    @GetMapping(path = "{jobId:\\d+}")
    public AssetSttJobResponse getJob(@PathVariable long assetId, @PathVariable long jobId) {
        return assetSttJobService.getJob(assetId, jobId);
    }

    @GetMapping(path = "{jobId:\\d+}/resource")
    public ResourceRegion getResource(@PathVariable long assetId, @PathVariable long jobId,
        @RequestHeader HttpHeaders httpHeaders) throws IOException {

        HttpRange httpRange = httpHeaders.getRange().stream().findFirst().orElse(null);

        return assetSttJobResourceService.getResourceWithRange(httpRange, Id.of(AssetSttJob.class, jobId));
    }

}
