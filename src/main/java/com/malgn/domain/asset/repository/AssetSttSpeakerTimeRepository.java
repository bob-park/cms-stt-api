package com.malgn.domain.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.asset.entity.AssetSttSpeakerTime;
import com.malgn.domain.asset.repository.query.AssetSttSpeakerTimeQueryRepository;

public interface AssetSttSpeakerTimeRepository extends JpaRepository<AssetSttSpeakerTime, Long>,
    AssetSttSpeakerTimeQueryRepository {
}
