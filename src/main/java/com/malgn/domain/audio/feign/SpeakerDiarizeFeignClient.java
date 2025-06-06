package com.malgn.domain.audio.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;

@FeignClient(name = "speaker-diarize-api", contextId = "speaker-diarize-api")
public interface SpeakerDiarizeFeignClient {

    @PostMapping(path = "diarize", consumes = "multipart/form-data")
    List<AudioSpeakerDiarizationResponse> diarize(@RequestPart("file") MultipartFile audio,
        @RequestPart("numSpeakers") int numSpeakers);

}
