package com.malgn.domain.audio.model;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record AudioSpeakerDiarizationResponse(String speaker,
                                              BigDecimal start,
                                              BigDecimal end) {
}
