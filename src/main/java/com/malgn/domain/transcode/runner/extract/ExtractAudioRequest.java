package com.malgn.domain.transcode.runner.extract;

import lombok.Builder;

import com.malgn.domain.transcode.runner.TranscodeRequest;

@Builder
public record ExtractAudioRequest(String source,
                                  String dest)
    implements TranscodeRequest {
}
