package com.malgn.domain.audio.model.ai;

import java.math.BigDecimal;
import java.util.List;

public record OpenAiAudioTranscriptionResponse(String text,
                                               BigDecimal duration,
                                               List<OpenAiAudioTranscriptionSegment> segments) {
}
