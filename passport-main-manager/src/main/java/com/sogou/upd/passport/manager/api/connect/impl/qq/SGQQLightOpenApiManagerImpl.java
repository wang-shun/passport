package com.sogou.upd.passport.manager.api.connect.impl.qq;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.HttpConstant;
import com.sogou.upd.passport.common.parameter.AccountTypeEnum;
import com.sogou.upd.passport.common.utils.JacksonJsonMapperUtil;
import com.sogou.upd.passport.manager.api.BaseProxyManager;
import com.sogou.upd.passport.manager.api.connect.QQLightOpenApiManager;
import com.sogou.upd.passport.manager.api.connect.form.qq.QQLightOpenApiParams;
import com.sogou.upd.passport.model.app.ConnectConfig;
import com.sogou.upd.passport.oauth2.common.utils.qqutils.OpenApiV3;
import com.sogou.upd.passport.oauth2.common.utils.qqutils.OpensnsException;
import com.sogou.upd.passport.service.app.ConnectConfigService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 13-11-28
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 */
@Component("sgQQLightOpenApiManager")
public class SGQQLightOpenApiManagerImpl extends BaseProxyManager implements QQLightOpenApiManager {

    private static final Logger logger = LoggerFactory.getLogger(SGQQLightOpenApiManagerImpl.class);

    @Autowired
    private ConnectConfigService connectConfigService;

    @Override
    public String executeQQOpenApi(String openId, String openKey, QQLightOpenApiParams qqParams, String thirdAppId) throws Exception {
        String resp;
        try {
            //QQ提供的openapi服务器
            String serverName = CommonConstant.QQ_SERVER_NAME_GRAPH;
            //应用的基本信息，搜狗在QQ的第三方appid与appkey
            String userId = qqParams.getUserid();
            int provider = AccountTypeEnum.getAccountType(userId).getValue();
            ConnectConfig connectConfig = connectConfigService.queryConnectConfigByAppId(thirdAppId, provider);
            if (connectConfig == null) {
                throw new Exception("thirdAppid error, thirdAppid:" + thirdAppId);
            }
            String sgAppKey = connectConfig.getAppKey();     //搜狗在QQ的appid
            String sgAppSecret = connectConfig.getAppSecret(); //搜狗在QQ的appkey
            OpenApiV3 sdkSG = createOpenApiByApp(sgAppKey, sgAppSecret, serverName);
            //调用代理第三方接口，点亮或熄灭QQ图标
            resp = executeQQLightOpenApi(sdkSG, openId, openKey, qqParams);
        } catch (Exception e) {
            logger.error("Execute Api Is Failed :", e);
            throw new Exception("Execute Api Is Failed:", e);
        }
        return resp;
    }


    private String executeQQLightOpenApi(OpenApiV3 sdk, String openid, String openkey, QQLightOpenApiParams qqLightOpenApiParams) {
        String resp = null;
        try {
            // 指定OpenApi Cgi名字
            String scriptName = qqLightOpenApiParams.getOpenApiName();
            // 指定HTTP请求协议类型,目前代理接口走的都是HTTP请求，所以需要sig签名，如果为HTTPS请求，则不需要sig签名
            String protocol = CommonConstant.HTTPS;
            // 填充URL请求参数,用来生成sig签名
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("openid", openid);
            params.put("openkey", openkey);
            ObjectMapper objectMapper = JacksonJsonMapperUtil.getMapper();
            HashMap<String, String> maps;
            Object paramsObj = qqLightOpenApiParams.getParams();
            if (paramsObj != null) {
                maps = objectMapper.readValue(paramsObj.toString(), HashMap.class);
                if (!maps.isEmpty()) {
                    Set<Map.Entry<String, String>> entrySet = maps.entrySet();
                    if (!entrySet.isEmpty() && entrySet.size() > 0) {
                        for (Map.Entry<String, String> entry : entrySet) {
                            params.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            //目前QQ SDK只提供了post请求，且已经与QQ确认过，他们目前所有的开放接口post请求都可以正确访问
            String method = HttpConstant.HttpMethod.POST;
            resp = sdk.api(scriptName, params, protocol, method);
        } catch (IOException ioe) {
            logger.error("Transfer Object To Map Failed :", ioe);
        } catch (OpensnsException oe) {
            logger.error(String.format("Request Failed.code:{}, msg:{}", oe.getErrorCode(), oe.getMessage()), oe);
        } catch (Exception e) {
            logger.warn("Execute Api Is Failed :", e);
        }
        return resp;
    }

    private OpenApiV3 createOpenApiByApp(String appKey, String appSecret, String serverName) {
        OpenApiV3 sdk = new OpenApiV3(appKey, appSecret);
        sdk.setServerName(serverName);
        return sdk;
    }
}