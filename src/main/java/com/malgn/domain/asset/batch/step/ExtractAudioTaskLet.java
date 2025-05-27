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
import com.malgn.domain.transcode.runner.extract.ExtractAudioRequest;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@Slf4j
@RequiredArgsConstructor
public class ExtractAudioTaskLet implements Tasklet {

    private final AppProperties properties;
    private final AssetSttJobRepository assetSttJobRepository;
    private final ExtractAudioRunner runner;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        JobParameters parameters = contribution.getStepExecution().getJobParameters();

        Long id = parameters.getLong("id");
        String source = parameters.getString("sourcePath");
        String dest = parameters.getString("audioPath");

        String dirPath = properties.baseLocation().getFile().getAbsolutePath();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AssetSttJob.class, id));

        log.debug("started extract audio...");

        runner.run(
            ExtractAudioRequest.builder()
                .source(dirPath + File.separatorChar + source)
                .dest(dirPath + File.separatorChar + dest)
                .build());

        log.debug("completed extract audio...");

        assetSttJob.updateAudioPath(dest);

        return RepeatStatus.FINISHED;
    }
}
