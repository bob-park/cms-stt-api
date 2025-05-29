package com.malgn.domain.asset.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttTextResponse;
import com.malgn.domain.asset.service.v1.AssetSttTextServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/assets/{assetId:\\d+}/stt/jobs/{jobId:\\d+}/texts")
public class AssetSttTextControllerV1 {

    private final AssetSttTextServiceV1 assetSttTextService;

    @GetMapping(path = "")
    public Page<AssetSttTextResponse> getAll(@PathVariable Long assetId, @PathVariable Long jobId,
        @PageableDefault(size = 25) Pageable pageable) {
        return assetSttTextService.getAll(Id.of(AssetSttJob.class, jobId), pageable);
    }

}
