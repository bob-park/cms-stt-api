package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttJob.*;
import static com.malgn.domain.asset.entity.QAssetSttSpeaker.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.entity.QAssetSttJob;
import com.malgn.domain.asset.entity.QAssetSttSpeaker;
import com.malgn.domain.asset.repository.query.AssetSttSpeakerQueryRepository;

@RequiredArgsConstructor
public class AssetSttSpeakerQueryRepositoryImpl implements AssetSttSpeakerQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<AssetSttSpeaker> getAll(long jobId) {
        return query.selectFrom(assetSttSpeaker)
            .join(assetSttSpeaker.job, assetSttJob).fetchJoin()
            .where(assetSttJob.id.eq(jobId))
            .orderBy(assetSttSpeaker.id.asc())
            .fetch();
    }
}
