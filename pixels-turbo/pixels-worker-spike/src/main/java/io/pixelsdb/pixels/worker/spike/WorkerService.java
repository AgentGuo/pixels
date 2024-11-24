package io.pixelsdb.pixels.worker.spike;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.pixelsdb.pixels.common.physical.StorageProvider;
import io.pixelsdb.pixels.common.turbo.Input;
import io.pixelsdb.pixels.common.turbo.Output;
import io.pixelsdb.pixels.spike.handler.SpikeWorker;
import io.pixelsdb.pixels.worker.common.WorkerContext;
import io.pixelsdb.pixels.worker.common.WorkerMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

public class WorkerService<T extends WorkerInterface<I, O>, I extends Input, O extends Output>{
    private static final Logger log = LogManager.getLogger(WorkerService.class);

    final Class<T> handlerClass;
    final Class<I> typeParameterClass;

    public WorkerService(Class<T> handlerClass, Class<I> typeParameterClass)
    {
        this.handlerClass = handlerClass;
        this.typeParameterClass = typeParameterClass;
    }

    public SpikeWorker.CallWorkerFunctionResp execute(String workerPayLoad, String requestId)
    {
        I input = JSON.parseObject(workerPayLoad, typeParameterClass);
        O output;
        try
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            System.out.println("Current ClassLoader: " + loader);

            String classPath = System.getProperty("java.class.path");
            System.out.println("ClassPath: " + classPath);

//            ServiceLoader<StorageProvider> providerLoader = ServiceLoader.load(StorageProvider.class);
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            ServiceLoader<StorageProvider> providerLoader = ServiceLoader.load(StorageProvider.class, contextClassLoader);
            int classCnt = 0;
            log.error("Current ClassLoader: " + Thread.currentThread().getContextClassLoader());
            for (StorageProvider storageProvider : providerLoader)
            {
                log.error(String.format("storageProvider class: %s", storageProvider.getClass().getName()));
                classCnt++;
            }
            log.error(String.format("classCnt: %d", classCnt));
            WorkerContext context = new WorkerContext(LogManager.getLogger(handlerClass), new WorkerMetrics(), requestId);
            WorkerInterface<I, O> worker = handlerClass.getConstructor(WorkerContext.class).newInstance(context);
            log.info(String.format("execute input: %s",
                    JSON.toJSONString(input, SerializerFeature.DisableCircularReferenceDetect)));
            output = worker.handleRequest(input);
            log.info(String.format("get output successfully: %s", JSON.toJSONString(output)));
        } catch (Exception e)
        {
            throw new RuntimeException("Exception during process: ", e);
        }
        return SpikeWorker.CallWorkerFunctionResp.newBuilder()
                .setRequestId(requestId)
                .setPayload(JSON.toJSONString(output))
                .build();
    }
}