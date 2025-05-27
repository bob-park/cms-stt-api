package com.malgn.domain.asset.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.asset.model.AssetSttJobResponse;
import com.malgn.domain.asset.model.v1.CreateAssetSttJobRequestV1;
import com.malgn.domain.asset.service.v1.AssetSttJobServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/assets/{assetId:\\d+}/stt/jobs")
public class AssetSttJobControllerV1 {

    private final AssetSttJobServiceV1 assetSttJobService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public AssetSttJobResponse createJob(@PathVariable long assetId,
        @RequestBody CreateAssetSttJobRequestV1 createRequest) {
        return assetSttJobService.createJob(assetId, createRequest);
    }

}
