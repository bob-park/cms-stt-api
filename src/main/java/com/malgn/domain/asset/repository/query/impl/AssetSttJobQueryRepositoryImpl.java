package com.malgn.domain.asset.repository.query.impl;

import static com.malgn.domain.asset.entity.QAssetSttJob.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.querydsl.model.QueryDslPath;
import com.malgn.common.querydsl.utils.QueryRepositoryUtils;
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

    @Override
    public Page<AssetSttJob> findAll(Long assetId, Pageable pageable) {

        List<AssetSttJob> content =
            query.selectFrom(assetSttJob)
                .where(assetSttJob.assetId.eq(assetId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sort(pageable))
                .fetch();

        JPAQuery<Long> countQuery =
            query.select(assetSttJob.id.count())
                .from(assetSttJob)
                .where(assetSttJob.assetId.eq(assetId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] sort(Pageable pageable) {
        return QueryRepositoryUtils.sort(
            pageable,
            List.of(
                new QueryDslPath<>("createdDate", assetSttJob.createdDate)));
    }
}
