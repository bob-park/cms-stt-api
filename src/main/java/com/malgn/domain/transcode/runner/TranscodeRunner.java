package com.malgn.domain.transcode.runner;

public interface TranscodeRunner {

    void run(TranscodeRequest request);

    default boolean supports(TranscodeType type){
        return false;
    }

}
