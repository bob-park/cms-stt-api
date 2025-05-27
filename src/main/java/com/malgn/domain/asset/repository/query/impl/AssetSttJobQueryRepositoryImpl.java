package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttJob.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.type.task.TaskStatus;
import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.entity.QAssetSttJob;
import com.malgn.domain.asset.repository.query.AssetSttJobQueryRepository;

@RequiredArgsConstructor
public class AssetSttJobQueryRepositoryImpl implements AssetSttJobQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public AssetSttJob fetch() {

        return query.selectFrom(assetSttJob)
            .where(
                assetSttJob.isDeleted.eq(false),
                assetSttJob.status.eq(TaskStatus.WAITING))
            .orderBy(assetSttJob.createdDate.asc())
            .limit(1)
            .offset(0)
            .fetchOne();
    }
}
