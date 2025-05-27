package com.malgn.domain.asset.batch.step;

import java.io.File;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.malgn.common.exception.NotFoundException;
import com.malgn.configure.properties.AppProperties;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.audio.feign.SpeakerDiarizeFeignClient;

@Slf4j
@RequiredArgsConstructor
public class SpeakerDiarizeTaskLet implements Tasklet {

    private final AppProperties properties;

    private final SpeakerDiarizeFeignClient speakerDiarizeClient;

    private final AssetSttJobRepository assetSttJobRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        JobParameters parameters = contribution.getStepExecution().getJobParameters();

        String dirPath = properties.baseLocation().getFile().getAbsolutePath();

        Long id = parameters.getLong("id");

        AssetSttJob job =
            assetSttJobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, id));

        String absoluteAudioPath = dirPath + File.separatorChar + job.getAudioPath();

        return null;
    }
}
