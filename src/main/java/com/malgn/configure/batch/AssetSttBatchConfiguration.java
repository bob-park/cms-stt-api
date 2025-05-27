package com.malgn.configure.batch;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.type.task.TaskStatus;
import com.malgn.configure.properties.AppProperties;
import com.malgn.domain.asset.batch.step.ExtractAudioTaskLet;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.transcode.runner.extract.ExtractAudioRunner;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class AssetSttBatchConfiguration {

    private final AppProperties properties;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final AssetSttJobRepository assetSttJobRepository;

    @Bean
    public Job assetSttJob() {

        return new JobBuilder("assetSttJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(fetchJobStep())
            .next(extractAudioStep(null))
            .next(completeJobStep())
            .build();
    }

    @Bean
    public Step fetchJobStep() {
        return new StepBuilder("fetchJobStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {

                JobParameters parameters = contribution.getStepExecution().getJobParameters();

                Long id = parameters.getLong("id");

                AssetSttJob assetSttJob =
                    assetSttJobRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(AssetSttJob.class, id));

                assetSttJob.updateStatus(TaskStatus.PROCEEDING);

                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }

    @Bean
    public Step extractAudioStep(ExtractAudioRunner runner) {
        return new StepBuilder("extractAudioStep", jobRepository)
            .tasklet(
                new ExtractAudioTaskLet(properties, assetSttJobRepository, runner),
                transactionManager)
            .build();
    }

    @Bean
    public Step completeJobStep() {
        return new StepBuilder("completeJobStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {

                JobParameters parameters = contribution.getStepExecution().getJobParameters();

                Long id = parameters.getLong("id");

                AssetSttJob assetSttJob =
                    assetSttJobRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(AssetSttJob.class, id));

                assetSttJob.updateStatus(TaskStatus.SUCCESS);

                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }

}
