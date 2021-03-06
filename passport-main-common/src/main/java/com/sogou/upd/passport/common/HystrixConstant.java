package com.sogou.upd.passport.common;

/**
 * Created with IntelliJ IDEA.
 * User: nahongxu
 * Date: 15-4-9
 * Time: 上午10:51
 * To change this template use File | Settings | File Templates.
 */
public class HystrixConstant {
    //hystrix default global configurations
    public static final String DEFAULT_GLOBAL_ENABLED = "false";   //默认总开关:关闭
    public static final String DEFAULT_QQ_HYSTRIX_ENABLED = "false";//默认QQ开关：关闭
    public static final String DEFAULT_KAFKA_HYSTRIX_ENABLED = "false";//默认kafka开关：关闭
    public static final String DEFAULT_KAFKA_CHOOSE_THREAD_MODE = "true";//默认采用线程池隔离kafka
    public static final String DEFAULT_REQUEST_CACHE_ENABLED = "false";    //默认不开启request cache
    public static final String DEFAULT_ERROR_THRESHOLD_PERCENTAGE = "50";  //默认错误率阈值为70%
    public static final String DEFAULT_REQUEST_LOG_ENABLED = "true";
    public static final String DEFAULT_BREAKER_FORCE_OPEN = "false";
    public static final String DEFAULT_BREAKER_FORCE_CLOSE = "false";
    public static final String DEFAULT_BREAKER_SLEEP_WINDOW = "500";


    //QQ依赖调用
    public static final String DEFAULT_QQ_URL = "https://graph.qq.com";
    public static final String DEFAULT_QQ_SG_POOL_CORESIZE = "15";
    public static final String DEFAULT_QQ_CONNECT_POOL_CORESIZE = "100";
    public static final String DEFAULT_QQ_OAUTH_POOL_CORESIZE = "15";
    public static final String DEFAULT_QQ_TIMEOUT = "2000";//ms
    public static final String DEFAULT_QQ_SG_REQUESTVOLUME = "20";
    public static final String DEFAULT_QQ_CONNECT_REQUESTVOLUME = "20";
    public static final String DEFAULT_QQ_OAUTH_REQUESTVOLUME = "20";

    //kafka依赖调用
    public static final String DEFAULT_KAFKA_HYSTRIX_THREADPOOL_CORESIZE = "15";
    public static final String DEFAULT_KAFKA_TIMEOUT = "100";// ms
    public static final String DEFAULT_KAFKA_REQUESTVOLUME_THRESHOLD = "40";
    public static final String DEFAULT_KAFKA_SEMAPHORE_THRESHOLD = "10";
    public static final String DEFAULT_FALLBACK_SEMAPHORE_THRESHOLD = "10";

    //好友链腾讯云ip，port
//    public static final String DEFAULT_QCLOUD_IP_PORT="115.159.57.127:8888";
    public static final String DEFAULT_QCLOUD_IP_PORT="203.195.255.86:8888";

    //property name
    public static final String PROPERTY_GLOBAL_ENABLED = "globalEnabled";
    public static final String PROPERTY_QQ_HYSTRIX_ENABLED = "qqHystrixEnabled";
    public static final String PROPERTY_BREAKER_FORCE_OPEN = "breakerForceOpen";
    public static final String PROPERTY_BREAKER_FORCE_CLOSE = "breakerFoceClose";
    public static final String PROPERTY_BREAKER_SLEEP_WINDOW = "circuitBreakerSleepWindow";
    public static final String PROPERTY_KAFKA_HYSTRIX_ENABLED = "kafkaHystrixEnabled";
    public static final String PROPERTY_KAFKA_CHOOSE_THREAD_MODE = "kafkaChooseThreadMode";
    public static final String PROPERTY_REQUEST_CACHE_ENABLED = "requestCacheEnabled";
    public static final String PROPERTY_REQUEST_LOG_ENABLED = "requestLogEnabled";
    public static final String PROPERTY_ERROR_THRESHOLD_PERCENTAGE = "errorThresholdPercentage";
    public static final String PROPERTY_QQ_URL = "qqUrl";
    public static final String PROPERTY_QQ_SG_POOL_CORESIZE = "qqSGPoolCoreSize";
    public static final String PROPERTY_QQ_CONNECT_POOL_CORESIZE = "qqConnectPoolCoreSize";
    public static final String PROPERTY_QQ_OAUTH_POOL_CORESIZE = "qqOAuthPoolCoreSize";
    public static final String PROPERTY_QQ_TIMEOUT = "qqTimeout";
    public static final String PROPERTY_QQ_SG_REQUESTVOLUME = "qqSGRequestVolume";
    public static final String PROPERTY_QQ_CONNECT_REQUESTVOLUME = "qqConnectRequestVolume";
    public static final String PROPERTY_QQ_OAUTH_REQUESTVOLUME = "qqOAuthRequestVolume";


    public static final String PROPERTY_KAFKA_HYSTRIX_THREADPOOL_CORESIZE = "kafkaHystrixThreadPoolCoreSize";
    public static final String PROPERTY_KAFKA_TIMEOUT = "kafkaTimeout";
    public static final String PROPERTY_KAFKA_REQUESTVOLUME_THRESHOLD = "kafkaRequestVolumeThreshold";
    public static final String PROPERTY_KAFKA_SEMAPHORE_THRESHOLD = "kafkaSemaphoreThreshold";
    public static final String PROPERTY_FALLBACK_SEMAPHORE_THRESHOLD = "fallbackSemaphoreThreshold";

    //fallback reason
    public static final String FALLBACK_REASON_EXCUTE_FAILED = "isExcutedFailed";
    public static final String FALLBACK_REASON_TIMEOUT = "isTimeout";
    public static final String FALLBACK_REASON_REJECTED = "isRejected";
    public static final String FALLBACK_REASON_SHORT_CIRCUITED = "isShortCircuited";
    public static final String FALLBACK_REASON_CANNOT_FALLBACK = "cannotFallback";
    public static final String FALLBACK_REASON_UNKNOWN_REASON = "isUnkownReason";
    public static final String PROPERTY_QCLOUD_IP_PORT="qCloudIpPort";




}
