package com.malgn.domain.audio.service;

import com.malgn.domain.audio.model.AudioSpeechToTextRequest;
import com.malgn.domain.audio.model.AudioSpeechToTextResponse;

public interface AudioTranscribeService {

    AudioSpeechToTextResponse transcription(AudioSpeechToTextRequest request);

}
