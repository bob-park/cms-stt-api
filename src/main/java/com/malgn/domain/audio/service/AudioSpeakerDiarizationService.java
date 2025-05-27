package com.malgn.domain.audio.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;

public interface AudioSpeakerDiarizationService {

    List<AudioSpeakerDiarizationResponse> diarize(MultipartFile audio);

}
