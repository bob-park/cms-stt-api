package com.malgn.domain.asset.model;

import com.malgn.common.model.CommonResponse;
import com.malgn.common.type.task.TaskStatus;

public interface AssetSttJobResponse extends CommonResponse {

    Long id();

    Long assetId();

    TaskStatus status();

    String sourcePath();

    Boolean isDeleted();

}
