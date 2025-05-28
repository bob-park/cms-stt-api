package com.malgn.domain.asset.repository.query.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.repository.query.AssetSttTextQueryRepository;

@RequiredArgsConstructor
public class AssetSttTextQueryRepositoryImpl implements AssetSttTextQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<AssetSttText> getText(double startTime, double endTime) {
        return List.of();
    }
}
