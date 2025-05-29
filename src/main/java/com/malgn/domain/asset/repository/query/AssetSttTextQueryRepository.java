package com.malgn.domain.asset.repository.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.asset.entity.AssetSttText;

public interface AssetSttTextQueryRepository {

    List<AssetSttText> getAll(Long jobId);

    Page<AssetSttText> getAll(Long jobId, Pageable pageable);

}
