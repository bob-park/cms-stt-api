package com.malgn.domain.audio.parser;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record TimeLineText(BigDecimal startTime,
                           BigDecimal endTime,
                           String text) {
}
