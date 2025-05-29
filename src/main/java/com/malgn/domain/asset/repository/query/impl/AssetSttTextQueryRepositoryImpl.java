package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttJob.*;
import static com.malgn.domain.asset.entity.QAssetSttText.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.entity.QAssetSttJob;
import com.malgn.domain.asset.entity.QAssetSttText;
import com.malgn.domain.asset.repository.query.AssetSttTextQueryRepository;

@RequiredArgsConstructor
public class AssetSttTextQueryRepositoryImpl implements AssetSttTextQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<AssetSttText> getAll(Long jobId) {
        return query.selectFrom(assetSttText)
            .join(assetSttText.job, assetSttJob).fetchJoin()
            .where(assetSttJob.id.eq(jobId))
            .orderBy(assetSttText.startTime.asc())
            .fetch();
    }
}
