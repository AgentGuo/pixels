package io.pixelsdb.pixels.worker.spike;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import io.pixelsdb.pixels.common.turbo.WorkerType;
import lombok.Data;

@JSONType
@Data
public class WorkerRequest {
    @JSONField(name = "workerType")

    private WorkerType workerType;
    @JSONField(name = "workerPayload")

    private String workerPayload;

    public WorkerRequest() {
    }

    public WorkerRequest(WorkerType workerType, String workerPayload) {
        this.workerType = workerType;
        this.workerPayload = workerPayload;
    }
}
