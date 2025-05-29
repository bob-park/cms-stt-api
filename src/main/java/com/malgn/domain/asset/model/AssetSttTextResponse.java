package com.malgn.domain.asset.model;

import java.math.BigDecimal;

import com.malgn.common.model.CommonResponse;

public interface AssetSttTextResponse extends CommonResponse {

    String id();

    BigDecimal startTime();

    BigDecimal endTime();

    AssetSttSpeakerResponse speaker();

    String text();

}
