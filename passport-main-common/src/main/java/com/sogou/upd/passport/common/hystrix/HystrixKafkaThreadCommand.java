package com.sogou.upd.passport.common.hystrix;


import com.netflix.hystrix.*;
import com.sogou.upd.passport.common.HystrixConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: nahongxu
 * Date: 15-4-9
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class HystrixKafkaThreadCommand extends HystrixCommand<Void> {

    private String infoToLog;

    private static final Logger logger = LoggerFactory.getLogger("hystrixLogger");
    private static final Logger kafkaLogger = LoggerFactory.getLogger("userLoggerKafka");

    private static boolean requestCacheEnable = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_REQUEST_CACHE_ENABLED));
    private static boolean requestLogEnable = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_REQUEST_LOG_ENABLED));
    private static boolean breakerForceOpen = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_BREAKER_FORCE_OPEN));
    private static boolean breakerForceClose = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_BREAKER_FORCE_CLOSE));
    private static int errorThresholdPercentage = Integer.parseInt(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_ERROR_THRESHOLD_PERCENTAGE));
    private static int kafkaHystrixThreadPoolCoreSize = Integer.parseInt(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_KAFKA_HYSTRIX_THREADPOOL_CORESIZE));
    private static final int kafkaTimeout = Integer.parseInt(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_KAFKA_TIMEOUT));
    private static final int kafkaRequestVolumeThreshold = Integer.parseInt(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_KAFKA_REQUESTVOLUME_THRESHOLD));
    private static final int fallbackSemaphoreThreshold = Integer.parseInt(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_FALLBACK_SEMAPHORE_THRESHOLD));

    public HystrixKafkaThreadCommand(String infoToLog) {

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixKafka"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("KafkaHystrixThreadCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HystrixThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        .withRequestCacheEnabled(requestCacheEnable)
                        .withRequestLogEnabled(requestLogEnable)
                        .withCircuitBreakerForceOpen(breakerForceOpen)
                        .withCircuitBreakerForceClosed(breakerForceClose)
                        .withCircuitBreakerErrorThresholdPercentage(errorThresholdPercentage)
                        .withExecutionIsolationThreadTimeoutInMilliseconds(kafkaTimeout)
                        .withCircuitBreakerRequestVolumeThreshold(kafkaRequestVolumeThreshold)
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(fallbackSemaphoreThreshold))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(kafkaHystrixThreadPoolCoreSize)));
        this.infoToLog = infoToLog;

    }


    @Override
    protected Void run() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
//        logger.warn("invoke Hystrix Kafka Thread Command...");
        kafkaLogger.info(infoToLog);
        return null;
    }

    @Override
    protected Void getFallback() {

        boolean isShortCircuited = isResponseShortCircuited();
        boolean isRejected = isResponseRejected();
        boolean isTimeout = isResponseTimedOut();
        boolean isFailed = isFailedExecution();
        if (isFailed) {
            logger.error("HystrixKafkaThreadCommand fallback isFailedExecution");
        } else if (isTimeout) {
            logger.error("HystrixKafkaThreadCommand fallback isTimeout");
        } else if (isRejected) {
            logger.error("HystrixKafkaThreadCommand fallback isRejected");
        } else if (isShortCircuited) {
            logger.error("HystrixKafkaThreadCommand fallback isShortCircuited");
        } else {
            logger.error("HystrixKafkaThreadCommand fallback unknown");
        }
        return null;
    }

}
