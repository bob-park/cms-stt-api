package com.malgn.domain.audio.client;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestClient;

import com.malgn.domain.audio.model.ai.OpenAiAudioTranscriptionResponse;

@Slf4j
public class OpenAiAudioTranscriptionClient {

    private static final String MODEL = "whisper-1";
    private static final String RESPONSE_FORMAT = "verbose_json";
    private static final String PROMPT = """
        토론을 하고 있으며, 대화의 한문장씩 추출해줘
        """;

    private final RestClient restClient;

    public OpenAiAudioTranscriptionClient(String apiKey, String baseUrl) {

        this.restClient =
            RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();

    }

    public OpenAiAudioTranscriptionResponse transcribe(Resource audio) {

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", audio);
        bodyBuilder.part("model", MODEL);
        bodyBuilder.part("response_format", RESPONSE_FORMAT);
        bodyBuilder.part("prompt", PROMPT);

        return restClient.post()
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(bodyBuilder.build())
            .retrieve()
            .body(OpenAiAudioTranscriptionResponse.class);

    }

}
