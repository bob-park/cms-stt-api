package com.malgn.domain.audio.controllver.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.audio.model.AudioSpeechToTextResponse;
import com.malgn.domain.audio.model.v1.AudioSpeechToTextRequestV1;
import com.malgn.domain.audio.service.v1.AudioTranscribeServiceV1;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/audio/transcribe")
public class AudioTranscribeControllerV1 {

    private final AudioTranscribeServiceV1 audioTranscribeService;

    @PostMapping(path = "")
    public AudioSpeechToTextResponse transcribe(AudioSpeechToTextRequestV1 request) {
        return audioTranscribeService.transcription(request);
    }

}
