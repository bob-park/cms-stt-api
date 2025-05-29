package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttSpeaker.*;
import static com.malgn.domain.asset.entity.QAssetSttSpeakerTime.*;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.repository.query.AssetSttSpeakerTimeQueryRepository;

@RequiredArgsConstructor
public class AssetSttSpeakerTimeQueryRepositoryImpl implements AssetSttSpeakerTimeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public AssetSttSpeakerTime getSpeakerTime(Long jobId, BigDecimal from, BigDecimal to) {

        return query.selectFrom(assetSttSpeakerTime)
            .leftJoin(assetSttSpeakerTime.speaker, assetSttSpeaker).fetchJoin()
            .where(
                assetSttSpeaker.job.id.eq(jobId),
                Expressions.asNumber(from).between(assetSttSpeakerTime.startTime, assetSttSpeakerTime.endTime)
                    .or(Expressions.asNumber(to).between(assetSttSpeakerTime.startTime, assetSttSpeakerTime.endTime)))
            .orderBy(
                assetSttSpeakerTime.startTime.subtract(from)
                    .add(assetSttSpeakerTime.endTime.subtract(to)).asc())
            .limit(1)
            .offset(0)
            .fetchOne();
    }
}
