package com.malgn.domain.asset.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.apache.commons.io.FilenameUtils;

import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@Slf4j
@RequiredArgsConstructor
@Component
public class AssetSttJobScheduler {

    private final AssetSttJobRepository assetSttJobRepository;

    private final Job assetSttJob;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 5_000, initialDelay = 10_000)
    public void launchJob() throws Exception {

        AssetSttJob fetch = assetSttJobRepository.fetch();

        if (fetch == null) {
            log.debug("no job...");
            return;
        }

        String source = fetch.getSourcePath();
        String dest =
            FilenameUtils.getFullPath(source) +
                FilenameUtils.getBaseName(source) + "." + ExtractAudioRunner.AUDIO_EXTENSION;

        JobParameters parameters =
            new JobParametersBuilder()
                .addLong("id", fetch.getId())
                .addString("sourcePath", source)
                .addString("audioPath", dest)
                .toJobParameters();

        jobLauncher.run(assetSttJob, parameters);

        log.debug("job launched...");

    }

}
