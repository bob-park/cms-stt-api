package com.malgn.domain.asset.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.asset.entity.AssetSttJob;
import com.malgn.domain.asset.repository.query.AssetSttJobQueryRepository;

public interface AssetSttJobRepository extends JpaRepository<AssetSttJob, Long>, AssetSttJobQueryRepository {
}
