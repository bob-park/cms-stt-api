package com.malgn.domain.asset.event.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.configure.properties.AppProperties;
import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.asset.entity.AssetSttAudio;
import com.malgn.domain.asset.entity.AssetSttAudioType;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.event.AssetSttJobEventType;
import com.malgn.domain.asset.event.AudioTranscribeCompletedEventPayload;
import com.malgn.domain.asset.event.SpeakerDiarizeCompleteEventPayload;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.repository.AssetSttTextRepository;
import com.malgn.domain.audio.parser.SrtFormatParser;
import com.malgn.domain.audio.parser.TimeLineText;

@Slf4j
@RequiredArgsConstructor
@Component
public class AudioTranscribeHandler implements CommandHandler<SpeakerDiarizeCompleteEventPayload> {

    private final AppProperties properties;

    private final OutboxEventPublisher publisher;

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    private final AssetSttJobRepository assetSttJobRepository;
    private final AssetSttTextRepository assetSttTextRepository;

    @Override
    public void handle(Event<SpeakerDiarizeCompleteEventPayload> event) {

        SpeakerDiarizeCompleteEventPayload payload = event.getPayload();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(payload.id())
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, payload.id()));

        String dir = null;
        try {
            dir = properties.baseLocation().getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

        List<AssetSttAudio> audios =
            assetSttJob.getAudios().stream()
                .filter(item -> item.getType() == AssetSttAudioType.SEGMENT)
                .toList();

        for (AssetSttAudio audio : audios) {
            String absoluteAudioPath = dir + File.separatorChar + audio.getAudioPath();

            AudioTranscriptionPrompt prompt =
                new AudioTranscriptionPrompt(
                    new FileSystemResource(absoluteAudioPath),
                    OpenAiAudioTranscriptionOptions.builder()
                        .model("whisper-1")
                        .language("ko")
                        .responseFormat(TranscriptResponseFormat.SRT)
                        .build());

            AudioTranscriptionResponse response = transcriptionModel.call(prompt);
            String contents = response.getResult().getOutput();

            List<TimeLineText> texts = SrtFormatParser.parse(contents);

            for (TimeLineText text : texts) {

                AssetSttText createdText =
                    AssetSttText.builder()
                        .startTime(text.startTime().add(audio.getStartTime()))
                        .endTime(text.endTime().add(audio.getStartTime()))
                        .text(text.text())
                        .build();

                assetSttJob.addText(createdText);

                createdText = assetSttTextRepository.save(createdText);

                log.debug("created asset stt text. ({})", createdText);

            }
        }

        publisher.publish(
            AssetSttJobEventType.AUDIO_TRANSCRIBE_COMPLETED,
            AudioTranscribeCompletedEventPayload.builder()
                .id(assetSttJob.getId())
                .build());

    }

    @Override
    public boolean supports(Event<SpeakerDiarizeCompleteEventPayload> event) {
        return event.getType() == AssetSttJobEventType.SPEAKER_DIARIZE_COMPLETED;
    }
}
