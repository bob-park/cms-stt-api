package com.malgn.domain.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.asset.entity.AssetSttText;
import com.malgn.domain.asset.repository.query.AssetSttTextQueryRepository;

public interface AssetSttTextRepository extends JpaRepository<AssetSttText, Long>, AssetSttTextQueryRepository {
}
