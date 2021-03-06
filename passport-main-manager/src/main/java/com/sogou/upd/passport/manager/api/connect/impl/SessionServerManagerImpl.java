package com.sogou.upd.passport.manager.api.connect.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.LoginConstant;
import com.sogou.upd.passport.common.model.httpclient.RequestModel;
import com.sogou.upd.passport.common.parameter.HttpMethodEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.common.utils.JacksonJsonMapperUtil;
import com.sogou.upd.passport.common.utils.SGHttpClient;
import com.sogou.upd.passport.manager.ManagerHelper;
import com.sogou.upd.passport.manager.api.SessionServerUrlConstant;
import com.sogou.upd.passport.manager.api.connect.SessionServerManager;
import com.sogou.upd.passport.model.app.AppConfig;
import com.sogou.upd.passport.service.account.AccountService;
import com.sogou.upd.passport.service.app.AppConfigService;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * User: mayan
 * Date: 13-12-4
 * Time: 下午8:44
 */
@Component("sessionServerManager")
public class SessionServerManagerImpl implements SessionServerManager {
    private static ObjectMapper jsonMapper = JacksonJsonMapperUtil.getMapper();

    private static final Logger logger = LoggerFactory.getLogger(SessionServerManagerImpl.class);

    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private AccountService accountService;

    private Map<String, String> buildHttpSessionParam(String sgid) {

        AppConfig appConfig = appConfigService.queryAppConfigByClientId(CommonConstant.SGPP_DEFAULT_CLIENTID);

        int clientId = appConfig.getClientId();
        String serverSecret = appConfig.getServerSecret();
        long ct = System.currentTimeMillis();

        String code = ManagerHelper.generatorCode(sgid, clientId, serverSecret, ct);

        Map<String, String> params = Maps.newHashMap();

        params.put("client_id", String.valueOf(clientId));
        params.put("code", code);
        params.put("ct", String.valueOf(ct));
        params.put(LoginConstant.COOKIE_SGID, sgid);
        return params;
    }

    private Map<String, String> buildHttpSessionParam(String prefix, String passportId) {

        AppConfig appConfig = appConfigService.queryAppConfigByClientId(CommonConstant.SGPP_DEFAULT_CLIENTID);

        int clientId = appConfig.getClientId();
        String serverSecret = appConfig.getServerSecret();
        long ct = System.currentTimeMillis();

        String code = ManagerHelper.generatorCode(passportId, clientId, serverSecret, ct);

        Map<String, String> params = Maps.newHashMap();

        params.put("client_id", String.valueOf(clientId));
        params.put("code", code);
        params.put("ct", String.valueOf(ct));
        params.put("prefix", prefix);
        params.put("passportId", passportId);
        return params;
    }

    private Map<String, String> buildHttpSessionParam(String sgid, int clientId) {

        AppConfig appConfig = appConfigService.queryAppConfigByClientId(clientId);

        String serverSecret = appConfig.getServerSecret();
        long ct = System.currentTimeMillis();

        String code = ManagerHelper.generatorCode(sgid, clientId, serverSecret, ct);

        Map<String, String> params = Maps.newHashMap();

        params.put("client_id", String.valueOf(clientId));
        params.put("code", code);
        params.put("ct", String.valueOf(ct));
        params.put(LoginConstant.COOKIE_SGID, sgid);
        return params;
    }

    @Override
    public Result createSession(String passportId) {
        return createSession(passportId, null, false);
    }

    @Override
    public Result createSession(String passportId, String weixinOpenId, boolean isWap) {
        Result result = new APIResultSupport(false);

        SessionResult sessionResult = null;
        try {
            // 获取账号前缀
            String prefix = accountService.getAccountPrefix(passportId);

            Map<String, String> params = buildHttpSessionParam(prefix, passportId);

            params.put("wap", Boolean.toString(isWap));
            if(StringUtils.isNotBlank(weixinOpenId)) {
                params.put("weixinOpenId", weixinOpenId);
            }

            RequestModel requestModel = new RequestModel(SessionServerUrlConstant.CREATE_SESSION);

            Set<Map.Entry<String, String>> paramsEntrySet = params.entrySet();
            for (Map.Entry<String, String> entry : paramsEntrySet) {
                requestModel.addParam(entry.getKey(), entry.getValue());
            }

            requestModel.setHttpMethodEnum(HttpMethodEnum.POST);

            String resultRequest = SGHttpClient.executeStr(requestModel);
            if (!Strings.isNullOrEmpty(resultRequest)) {
                sessionResult = jsonMapper.readValue(resultRequest, SessionResult.class);
                if ("0".equals(sessionResult.getStatus())) {
                    result.setSuccess(true);
                    String sgid = (String) sessionResult.getData().get(LoginConstant.COOKIE_SGID);
                    result.setDefaultModel(LoginConstant.COOKIE_SGID, sgid);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("createSession error! passportId:" + passportId + ",sessionResult:" + sessionResult, e);
        }
        logger.warn("createSession error! passportId:{} ,sessionResult:{}", passportId, sessionResult);
        result.setSuccess(false);
        result.setCode(ErrorUtil.ERR_CODE_CREATE_SGID_FAILED);
        return result;
    }

    @Override
    public Result removeSession(String sgid) {
        Result result = new APIResultSupport(false);
        try {
            Map<String, String> params = buildHttpSessionParam(sgid);

            RequestModel requestModel = new RequestModel(SessionServerUrlConstant.REMOVE_SESSION);

            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                requestModel.addParam(entry.getKey(), entry.getValue());
            }
            requestModel.setHttpMethodEnum(HttpMethodEnum.POST);

            String resultRequest = SGHttpClient.executeStr(requestModel);
            if (!Strings.isNullOrEmpty(resultRequest)) {
                SessionResult sessionResult = jsonMapper.readValue(resultRequest, SessionResult.class);
                if (result != null) {
                    if ("0".equals(sessionResult.getStatus())) {
                        result.setSuccess(true);
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("removeSession " + "sgid:" + sgid);
            }
        }
        return result;
    }

    @Override
    public Result getPassportIdBySgid(String sgid, String ip) {
        Result result = new APIResultSupport(false);

        Result verifyResult = verifySid(sgid, ip);
        if(verifyResult.isSuccess()) {
            String passportId = (String) verifyResult.getModels().get("passport_id");
            if(StringUtils.isNotBlank(passportId)) {
                result.setSuccess(true);
                result.getModels().put("passport_id", passportId);
            }
        }

        return result;
    }

    @Override
    public Result verifySid(String sgid, String ip) {
        Map<String, String> params = buildHttpSessionParam(sgid);
        params.put("user_ip", ip);

        return verifySid(params);
    }

    @Override
    public Result verifySid(String sgid, int clientId, String ip) {
        Map<String, String> params = buildHttpSessionParam(sgid, clientId);
        params.put("user_ip", ip);

        return verifySid(params);
    }

    private Result verifySid(Map<String, String> params) {
        Result result = new APIResultSupport(false);
        try {
            RequestModel requestModel = new RequestModel(SessionServerUrlConstant.VERIFY_SID);

            Set<Map.Entry<String, String>> set = params.entrySet();
            for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                requestModel.addParam(entry.getKey(), entry.getValue());
            }
            requestModel.setHttpMethodEnum(HttpMethodEnum.POST);

            String resultRequest = SGHttpClient.executeStr(requestModel);
            if (!Strings.isNullOrEmpty(resultRequest)) {
                Map mapResult = jsonMapper.readValue(resultRequest, Map.class);
                String status = (String) mapResult.get("status");
                String statusText = (String) mapResult.get("statusText");

                result.setCode(status);
                if(StringUtils.equals(status, "0")) {
                    result.setSuccess(true);
                }
                result.setMessage(statusText);
                Map<?, ?> models = (Map) mapResult.get("data");
                if(MapUtils.isNotEmpty(models)) {
                    result.setModels(models);
                }

                return result;
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("getPassportIdBySgid error! " + "sgid:" + params.get(LoginConstant.COOKIE_SGID));
            }
        }
        return result;
    }

}

class SessionResult {
    private String status;
    private String statusText;
    private Map<String, Object> data = Maps.newHashMap();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
