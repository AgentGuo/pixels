package io.pixelsdb.pixels.worker.spike;

import io.pixelsdb.pixels.spike.handler.RequestHandler;
import com.alibaba.fastjson.JSON;
import io.pixelsdb.pixels.planner.plan.physical.input.*;
import io.pixelsdb.pixels.planner.plan.physical.output.AggregationOutput;
import io.pixelsdb.pixels.planner.plan.physical.output.JoinOutput;
import io.pixelsdb.pixels.planner.plan.physical.output.PartitionOutput;
import io.pixelsdb.pixels.planner.plan.physical.output.ScanOutput;
import io.pixelsdb.pixels.spike.handler.SpikeWorker;

public class RequestHandlerImpl implements RequestHandler {
    @Override
    public SpikeWorker.CallWorkerFunctionResp execute(SpikeWorker.CallWorkerFunctionReq request) {
        // 获取请求的有效负载
        request.getRequestId();
        String payload = request.getPayload();
        WorkerRequest workerRequest = JSON.parseObject(payload, WorkerRequest.class);
        switch (workerRequest.getWorkerType())
        {
            case AGGREGATION:
            {
                WorkerService<AggregationWorker, AggregationInput, AggregationOutput> service = new WorkerService<>(AggregationWorker.class, AggregationInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case BROADCAST_CHAIN_JOIN:
            {
                WorkerService<BroadcastChainJoinWorker, BroadcastChainJoinInput, JoinOutput> service = new WorkerService<>(BroadcastChainJoinWorker.class, BroadcastChainJoinInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case BROADCAST_JOIN:
            {
                WorkerService<BroadcastJoinWorker, BroadcastJoinInput, JoinOutput> service = new WorkerService<>(BroadcastJoinWorker.class, BroadcastJoinInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case PARTITIONED_CHAIN_JOIN:
            {
                WorkerService<PartitionedChainJoinWorker, PartitionedChainJoinInput, JoinOutput> service = new WorkerService<>(PartitionedChainJoinWorker.class, PartitionedChainJoinInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case PARTITIONED_JOIN:
            {
                WorkerService<PartitionedJoinWorker, PartitionedJoinInput, JoinOutput> service = new WorkerService<>(PartitionedJoinWorker.class, PartitionedJoinInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case PARTITIONED_JOIN_STREAMING:
            {
                WorkerService<PartitionedJoinStreamWorker, PartitionedJoinInput, JoinOutput> service = new WorkerService<>(PartitionedJoinStreamWorker.class, PartitionedJoinInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case PARTITION:
            {
                WorkerService<PartitionWorker, PartitionInput, PartitionOutput> service = new WorkerService<>(PartitionWorker.class, PartitionInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case PARTITION_STREAMING:
            {
                WorkerService<PartitionStreamWorker, PartitionInput, PartitionOutput> service = new WorkerService<>(PartitionStreamWorker.class, PartitionInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case SCAN:
            {
                WorkerService<ScanWorker, ScanInput, ScanOutput> service = new WorkerService<>(ScanWorker.class, ScanInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            case SCAN_STREAM:
            {
                WorkerService<ScanStreamWorker, ScanInput, ScanOutput> service = new WorkerService<>(ScanStreamWorker.class, ScanInput.class);
                return service.execute(workerRequest.getWorkerPayload(), request.getRequestId());
            }
            default:
                throw new RuntimeException("Receive invalid worker type");
        }
    }
}
