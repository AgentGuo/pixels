package io.pixelsdb.pixels.worker.spike;
import io.pixelsdb.pixels.common.turbo.WorkerType;
import io.pixelsdb.pixels.planner.plan.physical.input.PartitionedChainJoinInput;
import io.pixelsdb.pixels.planner.plan.physical.output.JoinOutput;
import io.pixelsdb.pixels.worker.common.BasePartitionedChainJoinWorker;
import io.pixelsdb.pixels.worker.common.WorkerContext;

public class PartitionedChainJoinWorker extends BasePartitionedChainJoinWorker implements WorkerInterface<PartitionedChainJoinInput, JoinOutput>
{
    public PartitionedChainJoinWorker(WorkerContext context)
    {
        super(context);
    }

    @Override
    public JoinOutput handleRequest(PartitionedChainJoinInput input)
    {
        return process(input);
    }

    @Override
    public String getRequestId()
    {
        return this.context.getRequestId();
    }

    @Override
    public WorkerType getWorkerType()
    {
        return WorkerType.PARTITIONED_CHAIN_JOIN;
    }
}
