package com.malgn.configure.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.malgn.domain.audio.client.OpenAiAudioTranscriptionClient;

@Configuration
public class AiConfiguration {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.audio.speech.base-url}")
    private String baseUrl;

    @Bean
    public OpenAiAudioTranscriptionClient openAiAudioTranscriptionClient() {
        return new OpenAiAudioTranscriptionClient(apiKey, baseUrl);
    }

}
