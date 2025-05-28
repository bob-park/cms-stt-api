package com.malgn.domain.audio.model.ai;

import java.math.BigDecimal;

public record OpenAiAudioTranscriptionSegment(Long id,
                                              BigDecimal start,
                                              BigDecimal end,
                                              String text) {
}
