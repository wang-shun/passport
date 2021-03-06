package com.sogou.upd.passport.web.internal.debug;

import com.sogou.upd.passport.common.HystrixConstant;
import com.sogou.upd.passport.common.hystrix.HystrixConfigFactory;
import com.sogou.upd.passport.manager.api.account.form.SwitchHystrixParams;
import com.sogou.upd.passport.web.BaseController;
import com.sogou.upd.passport.web.annotation.InterfaceSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: nahongxu
 * Date: 15-5-13
 * Time: 下午3:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/internal")
public class SwitchHystrix extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger("hystrixLogger");

//    @InterfaceSecurity
    @RequestMapping(value = "/hystrix/switch", method = RequestMethod.POST)
    @ResponseBody
    public String switchHystrix(HttpServletRequest request, HttpServletResponse response, SwitchHystrixParams params) {

//        String operateIp = getIp(request);
//        String nahongxuIp = "10.129.204.218";
//        if (!nahongxuIp.equals(operateIp)) {
//            return "operate ip is denied";
//        }

        Boolean hystrixGlobalEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_GLOBAL_ENABLED));
        Boolean hystrixQQHystrixEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_QQ_HYSTRIX_ENABLED));
        Boolean hystrixKafkaHystrixEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_KAFKA_HYSTRIX_ENABLED));
        String qCloudIpPort=HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_QCLOUD_IP_PORT);

        logger.warn("before switch,globalEnabled:" + hystrixGlobalEnabled + ",qqEnabled:" + hystrixQQHystrixEnabled + ",kafkaEnabled:" + hystrixKafkaHystrixEnabled);
        logger.warn("before switch,qcloud ip port:" +qCloudIpPort);

        HystrixConfigFactory.modifyProperty(HystrixConstant.PROPERTY_GLOBAL_ENABLED, params.getGlobalEnabled().toString());
        HystrixConfigFactory.modifyProperty(HystrixConstant.PROPERTY_QQ_HYSTRIX_ENABLED, params.getQqHystrixEnabled().toString());
        HystrixConfigFactory.modifyProperty(HystrixConstant.PROPERTY_KAFKA_HYSTRIX_ENABLED, params.getKafkaHystrixEnabled().toString());
        HystrixConfigFactory.modifyProperty(HystrixConstant.PROPERTY_QCLOUD_IP_PORT,params.getQcloudIpPort());

        hystrixGlobalEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_GLOBAL_ENABLED));
        hystrixQQHystrixEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_QQ_HYSTRIX_ENABLED));
        hystrixKafkaHystrixEnabled = Boolean.parseBoolean(HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_KAFKA_HYSTRIX_ENABLED));
        qCloudIpPort=HystrixConfigFactory.getProperty(HystrixConstant.PROPERTY_QCLOUD_IP_PORT);
        logger.warn("after switch,globalEnabled:" + hystrixGlobalEnabled + ",qqEnabled:" + hystrixQQHystrixEnabled + ",kafkaEnabled:" + hystrixKafkaHystrixEnabled);
        logger.warn("after switch,qcloud ip port:" +qCloudIpPort);
        return "switch hystrix success";
    }

}
