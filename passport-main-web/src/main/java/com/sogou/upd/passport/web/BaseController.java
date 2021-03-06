package com.sogou.upd.passport.web;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.lang.StringUtil;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ServletUtil;
import com.sogou.upd.passport.manager.account.vo.AccountSecureInfoVO;
import com.sogou.upd.passport.model.app.AppConfig;
import com.sogou.upd.passport.service.app.AppConfigService;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private AppConfigService appConfigService;

    /**
     * 获取用户的源ip
     *
     * @param request
     * @return
     */
    protected static String getIp(HttpServletRequest request) {
        String sff = request.getHeader("X-Forwarded-For");// 根据nginx的配置，获取相应的ip
        if (Strings.isNullOrEmpty(sff)) {
            sff = request.getHeader("X-Real-IP");
        }
        if (Strings.isNullOrEmpty(sff)) {
            return Strings.isNullOrEmpty(request.getRemoteAddr()) ? "" : request.getRemoteAddr();
        }
        String[] ips = sff.split(",");
        String realip = ips[0];
        return realip;
    }

    /**
     * 获取是http或https协议
     *
     * @param req
     * @return
     */
    protected String getProtocol(HttpServletRequest req) {
        String httpsHeader = req.getHeader(CommonConstant.HTTPS_HEADER);
        String httpOrHttps = CommonConstant.HTTP;
        if (!org.apache.commons.lang.StringUtils.isBlank(httpsHeader) && httpsHeader.equals(CommonConstant.HTTPS_VALUE)) {
            httpOrHttps = CommonConstant.HTTPS;
        }
        return httpOrHttps;
    }


    public boolean isAccessAccept(int clientId, HttpServletRequest request) {
        String apiName = request.getRequestURI();
        apiName = apiName.substring(apiName.lastIndexOf("/") + 1, apiName.length());
        String requestIp = getIp(request);
        try {
            AppConfig appConfig = appConfigService.queryAppConfigByClientId(clientId);
            if (appConfig == null) {
                return false;
            }
            String scope = appConfig.getScope();
            if (!Strings.isNullOrEmpty(apiName) && !StringUtil.splitStringContains(scope, ",", apiName)) {
                return false;
            }
            String serverIp = appConfig.getServerIp();
            if (!Strings.isNullOrEmpty(requestIp) && !StringUtil.splitStringContains(serverIp, ",", requestIp)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("isAccessAccept error, api:" + apiName, e);
            return false;
        }
    }

    /**
     * 跳转到回跳地址
     *
     * @param response
     * @param ru
     * @param errorCode
     * @param errorMsg
     * @throws Exception
     */
    public void returnErrMsg(HttpServletResponse response, String ru, String errorCode, String errorMsg) throws Exception {
//        if (Strings.isNullOrEmpty(ru) || "域名不正确".equals(errorMsg)) {
//            ru = CommonConstant.DEFAULT_INDEX_URL;
//        }

        //fix invalid ru redirect 安全漏洞
        if (Strings.isNullOrEmpty(ru)) {
            ru = CommonConstant.DEFAULT_INDEX_URL;
        }

        if (StringUtils.contains(errorMsg, CommonConstant.DOMAIN_ERROR) || CommonConstant.DOMAIN_ERROR.equals(errorMsg)) {
            ru = CommonConstant.DEFAULT_INDEX_URL;
        }

        Map paramMap = Maps.newHashMap();
        paramMap.put("errorCode", errorCode);
        paramMap.put("errorMsg", errorMsg);
        ru = ServletUtil.applyOAuthParametersString(ru, paramMap);
        response.sendRedirect(ru);
        return;
    }

    /*
     * jsonp的cb参数相关方法
     */
    protected boolean isCleanString(String cb) {
        if (Strings.isNullOrEmpty(cb)) {
            return true;
        }
        String cleanValue = Jsoup.clean(cb, Whitelist.none());
        return cleanValue.equals(cb);
    }

    /**
     * 获取request header的输入法的UA标识，如果包含sogou_ime，则代表是输入法，否则返回空
     *
     * @param request
     * @return
     */
    protected String getHeaderUserAgent(HttpServletRequest request) {
        String ua = request.getHeader(CommonConstant.USER_AGENT);
        ua = !Strings.isNullOrEmpty(ua) && ua.contains(CommonConstant.SOGOU_IME_UA) ? ua : ""; //输入法的标识
        return ua;
    }

    /**
     * 获取浏览器 User-agent
     *
     * @param request
     * @return
     */
    protected String getUserAgent(HttpServletRequest request) {
        String user_agent = request.getHeader(CommonConstant.USER_AGENT);
        return Strings.isNullOrEmpty(user_agent) ? StringUtils.EMPTY : user_agent;
    }


    /**
     * 模糊处理邮箱和手机
     */
    protected void processSecureMailMobile(Result result) {
        AccountSecureInfoVO accountSecureInfoVO = (AccountSecureInfoVO) result.getDefaultModel();
        String sec_email = (String) result.getModels().get("sec_email");
        String sec_mobile = (String) result.getModels().get("sec_mobile");
        if (accountSecureInfoVO != null) {
            if (!Strings.isNullOrEmpty(sec_email)) {
                result.setDefaultModel("sec_email", accountSecureInfoVO.getSec_email());
            }
            if (!Strings.isNullOrEmpty(sec_mobile)) {
                result.setDefaultModel("sec_mobile", accountSecureInfoVO.getSec_mobile());
            }

        }
    }

    /**
     * 获取cookie
     *
     * @param request
     * @return
     */
    protected String getCookies(HttpServletRequest request) {
        Map<String, String> cookieMap = Maps.newLinkedHashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        Joiner.MapJoiner joiner = Joiner.on(CommonConstant.JOINER_SEPARATOR).withKeyValueSeparator(CommonConstant.KEY_VALUE_SEPARATOR);
        return joiner.join(cookieMap);
    }
    
    /**
     * 对头像地址进行 http / https 协议转换
     * @param request
     * @param result
     */
    protected void processAvatarUrl(HttpServletRequest request, Result result) {
        if(result == null) {
            return ;
        }
        
        String protocol = getProtocol(request);
    
        processUrl(result, "avatarurl", protocol);
        processUrl(result, "tiny_avatar", protocol);
        processUrl(result, "mid_avatar", protocol);
        processUrl(result, "large_avatar", protocol);
    }
    
    /**
     * 对 url 进行协议转换。将 url 中协议转换为参数 protocol 的协议。
     * @param result
     * @param key
     * @param protocol http https
     */
    protected void processUrl(Result result, String key, String protocol) {
        if(result.getModels().containsKey(key)) {
            String url = (String) result.getModels().get(key);
            url = StringUtils.equals(CommonConstant.HTTPS, protocol) ?
                        StringUtil.replaceHttpToHttps(url) : StringUtil.replaceHttpsToHttp(url);
            result.setDefaultModel(key, url);
        }
    }
}
