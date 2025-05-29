package com.malgn.domain.asset.service.v1;

import java.io.File;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.configure.properties.AppProperties;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.repository.AssetSttJobRepository;
import com.malgn.domain.asset.service.AssetSttJobResourceService;
import com.malgn.domain.asset.service.AssetSttJobService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AssetSttJobResourceServiceV1 implements AssetSttJobResourceService {

    private static final DataSize DEFAULT_CHUNK_SIZE = DataSize.ofMegabytes(5);

    private final AppProperties properties;

    private final AssetSttJobRepository assetSttJobRepository;

    @Override
    public ResourceRegion getResourceWithRange(HttpRange range, Id<AssetSttJob, Long> jobId)
        throws IOException {

        String dir = properties.baseLocation().getFile().getAbsolutePath();

        AssetSttJob assetSttJob =
            assetSttJobRepository.findById(jobId.getValue())
                .orElseThrow(() -> new NotFoundException(jobId));

        long start = 0;
        long end = 0;
        long rangeLength = 0;

        FileSystemResource resource = new FileSystemResource(dir + File.separatorChar + assetSttJob.getSourcePath());

        long contentLength = resource.contentLength();
        long chunkSize = DEFAULT_CHUNK_SIZE.toBytes();
        rangeLength = contentLength;

        if (range != null) {
            start = range.getRangeStart(contentLength);
            end = range.getRangeEnd(contentLength);
            rangeLength = Math.min(chunkSize, end - start + 1);
        }

        return new ResourceRegion(resource, start, rangeLength);
    }
}
