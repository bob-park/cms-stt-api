package com.malgn.domain.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.asset.entity.AssetSttSpeaker;
import com.malgn.domain.asset.repository.query.AssetSttSpeakerQueryRepository;

public interface AssetSttSpeakerRepository extends JpaRepository<AssetSttSpeaker, Long>,
    AssetSttSpeakerQueryRepository {
}
