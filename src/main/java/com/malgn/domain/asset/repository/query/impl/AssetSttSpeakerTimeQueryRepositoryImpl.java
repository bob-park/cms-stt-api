package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttSpeakerTime.*;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.entity.QAssetSttSpeakerTime;
import com.malgn.domain.asset.repository.query.AssetSttSpeakerTimeQueryRepository;

@RequiredArgsConstructor
public class AssetSttSpeakerTimeQueryRepositoryImpl implements AssetSttSpeakerTimeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public AssetSttSpeakerTime getSpeakerTime(BigDecimal from, BigDecimal to) {
        return query.selectFrom(assetSttSpeakerTime)
            .where(
                assetSttSpeakerTime.startTime.between(from, to),
                assetSttSpeakerTime.endTime.between(from, to))
            .orderBy(assetSttSpeakerTime.startTime.asc())
            .limit(1)
            .offset(0)
            .fetchOne();
    }
}
