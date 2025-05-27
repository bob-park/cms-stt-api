package com.malgn.domain.audio.model;

import org.springframework.web.multipart.MultipartFile;

public interface AudioSpeechToTextRequest {

    MultipartFile audio();

}
