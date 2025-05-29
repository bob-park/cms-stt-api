package com.malgn.domain.asset.controller.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.model.AssetSttSpeakerResponse;
import com.malgn.domain.asset.service.v1.AssetSttSpeakerServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/assets/{assetId:\\d+}/stt/jobs/{jobId:\\d+}/speakers")
public class AssetSttSpeakerControllerV1 {

    private final AssetSttSpeakerServiceV1 assetSttSpeakerService;

    @GetMapping(path = "")
    public List<AssetSttSpeakerResponse> getAll(@PathVariable Long assetId, @PathVariable Long jobId) {
        return assetSttSpeakerService.getAll(Id.of(AssetSttJob.class, jobId));
    }

}
