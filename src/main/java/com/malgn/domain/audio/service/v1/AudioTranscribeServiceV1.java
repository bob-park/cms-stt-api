package com.malgn.domain.audio.service.v1;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.audio.transcription.AudioTranscription;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import groovy.util.logging.Slf4j;

import com.malgn.domain.audio.model.AudioSpeechToTextRequest;
import com.malgn.domain.audio.model.AudioSpeechToTextResponse;
import com.malgn.domain.audio.model.v1.AudioSpeechToTextRequestV1;
import com.malgn.domain.audio.service.AudioTranscribeService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AudioTranscribeServiceV1 implements AudioTranscribeService {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @Override
    public AudioSpeechToTextResponse transcription(AudioSpeechToTextRequest request) {

        AudioSpeechToTextRequestV1 requestV1 = (AudioSpeechToTextRequestV1)request;

        checkArgument(isNotEmpty(requestV1.audio()), "audio must be provided.");

        AudioTranscriptionPrompt prompt =
            new AudioTranscriptionPrompt(
                requestV1.audio().getResource(),
                OpenAiAudioTranscriptionOptions.builder()
                    .model("whisper-1")
                    .language("ko")
                    .responseFormat(TranscriptResponseFormat.SRT)
                    .build());

        AudioTranscriptionResponse response = transcriptionModel.call(prompt);

        AudioTranscription result = response.getResult();


        return null;
    }
}
