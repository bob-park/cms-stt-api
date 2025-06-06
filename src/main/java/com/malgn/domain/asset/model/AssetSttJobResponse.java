package com.malgn.domain.asset.model;

import com.malgn.common.model.CommonResponse;
import com.malgn.common.type.task.TaskStatus;

public interface AssetSttJobResponse extends CommonResponse {

    String id();

    Long assetId();

    Integer numSpeakers();

    TaskStatus status();

    String sourcePath();

    Boolean isDeleted();

}
