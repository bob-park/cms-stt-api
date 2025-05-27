package com.malgn.domain.audio.controllver.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;
import com.malgn.domain.audio.service.v1.AudioSpeakerDiarizationServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/audio/speaker/diarize")
public class AudioSpeakerDiarizationControllerV1 {

    private final AudioSpeakerDiarizationServiceV1 speakerDiarizationService;

    @PostMapping(path = "")
    public List<AudioSpeakerDiarizationResponse> diarize(@RequestPart MultipartFile audio) {
        return speakerDiarizationService.diarize(audio);
    }

}
