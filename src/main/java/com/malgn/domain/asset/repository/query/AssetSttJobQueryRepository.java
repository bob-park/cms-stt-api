package com.malgn.domain.asset.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.asset.entity.AssetSttJob;

public interface AssetSttJobQueryRepository {

    AssetSttJob fetch();

    Page<AssetSttJob> findAll(Long assetId, Pageable pageable);

}
