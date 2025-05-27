package com.malgn.domain.audio.service.v1;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.malgn.domain.audio.feign.SpeakerDiarizeFeignClient;
import com.malgn.domain.audio.model.AudioSpeakerDiarizationResponse;
import com.malgn.domain.audio.model.CommonMultipartFile;
import com.malgn.domain.audio.service.AudioSpeakerDiarizationService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AudioSpeakerDiarizationServiceV1 implements AudioSpeakerDiarizationService {

    private final SpeakerDiarizeFeignClient speakerDiarizeClient;

    @Override
    public List<AudioSpeakerDiarizationResponse> diarize(MultipartFile audio) throws IOException {
        return speakerDiarizeClient.diarize(new CommonMultipartFile(audio.getBytes()));
    }
}
