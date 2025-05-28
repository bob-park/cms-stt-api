package com.malgn.domain.asset.repository.query;

import java.math.BigDecimal;

import com.malgn.domain.asset.entity.AssetSttSpeakerTime;

public interface AssetSttSpeakerTimeQueryRepository {

    AssetSttSpeakerTime getSpeakerTime(BigDecimal from, BigDecimal to);

}
