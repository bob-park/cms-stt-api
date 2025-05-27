package com.malgn.domain.audio.model.v1;

import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.audio.model.AudioSpeechToTextRequest;

public record AudioSpeechToTextRequestV1(MultipartFile audio)
    implements AudioSpeechToTextRequest {
}
