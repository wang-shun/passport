package com.sogou.upd.passport.manager.api.account.impl;

import com.sogou.upd.passport.common.lang.StringUtil;
import com.sogou.upd.passport.common.math.Coder;
import com.sogou.upd.passport.common.model.httpclient.RequestModel;
import com.sogou.upd.passport.common.model.httpclient.RequestModelXml;
import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.parameter.HttpMethodEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.SGHttpClient;
import com.sogou.upd.passport.manager.api.BaseProxyManager;
import com.sogou.upd.passport.manager.api.SHPPUrlConstant;
import com.sogou.upd.passport.manager.api.account.LoginApiManager;
import com.sogou.upd.passport.manager.api.account.form.AppAuthTokenApiParams;
import com.sogou.upd.passport.manager.api.account.form.AuthUserApiParams;
import com.sogou.upd.passport.manager.api.account.form.CreateCookieApiParams;
import com.sogou.upd.passport.manager.api.account.form.CreateCookieUrlApiParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 代理搜狐Passport的登录实现
 * User: ligang201716@sogou-inc.com
 * Date: 13-6-6
 * Time: 上午10:36
 */
@Component("proxyLoginApiManager")
public class ProxyLoginApiManagerImpl extends BaseProxyManager implements LoginApiManager {

    private static Logger log = LoggerFactory.getLogger(ProxyLoginApiManagerImpl.class);

    @Override
    public Result webAuthUser(AuthUserApiParams authUserParameters) {
        String userId = authUserParameters.getUserid();
        if (AccountDomainEnum.PHONE.equals(AccountDomainEnum.getAccountDomain(userId))) {
            authUserParameters.setUsertype(1);
        }
        authUserParameters.setPwdtype(1);
        RequestModelXml requestModelXml = new RequestModelXml(SHPPUrlConstant.AUTH_USER, SHPPUrlConstant.DEFAULT_REQUEST_ROOTNODE);
        requestModelXml.addParams(authUserParameters);
        return executeResult(requestModelXml);
    }

    @Override
    public Result appAuthToken(AppAuthTokenApiParams appAuthTokenApiParams) {
        appAuthTokenApiParams.setType(2);
        RequestModelXml requestModelXml = new RequestModelXml(SHPPUrlConstant.MOBILE_AUTH_TOKEN, SHPPUrlConstant.DEFAULT_REQUEST_ROOTNODE);
        requestModelXml.addParams(appAuthTokenApiParams);
        return executeResult(requestModelXml, appAuthTokenApiParams.getToken());
    }

    @Override
    public Result createCookie(CreateCookieApiParams createCookieApiParams) {
        Result result = new APIResultSupport(false);

        RequestModel requestModel = new RequestModel(SHPPUrlConstant.GET_COOKIE_VALUE);
        requestModel.setHttpMethodEnum(HttpMethodEnum.POST);
        requestModel.addParams(createCookieApiParams);
        if (createCookieApiParams.isAutologin()) {
            requestModel.addParam("persistentcookie", 1);
        } else {
            requestModel.addParam("persistentcookie", 0);
        }
        //由于SGPP对一些参数的命名和SHPP不一致，在这里做相应的调整
        this.paramNameAdapter(requestModel);
        long ct = System.currentTimeMillis();
        String code = createCookieApiParams.getUserid() + SHPPUrlConstant.COOKIE_KEY + ct;
        try {
            code = Coder.encryptMD5(code);
            requestModel.addParam("ct", ct);
            requestModel.addParam("code", code);
            String value = SGHttpClient.executeStr(requestModel);
            if (StringUtil.isBlank(value) || value.trim().length() < 20) {
                throw new RuntimeException("获取cookie值失败 userid=" + createCookieApiParams.getUserid() + " value=" + value);
            }
            result.setDefaultModel("ppinf", value);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("获取cookie值失败 userid=" + createCookieApiParams.getUserid(), e);
        }
        return result;
    }

    @Override
    public Result buildCreateCookieUrl(CreateCookieUrlApiParams createCookieUrlApiParams) {
        Result result = new APIResultSupport(false);
        try {
            String ru = createCookieUrlApiParams.getRu();
            String userId = createCookieUrlApiParams.getUserid();
            ru = URLEncoder.encode(ru, "UTF-8");
            long ct = System.currentTimeMillis();
            String code = userId + SHPPUrlConstant.APP_ID + SHPPUrlConstant.APP_KEY + ct;
            code = Coder.encryptMD5(code);
            StringBuilder urlBuilder = new StringBuilder(SHPPUrlConstant.SET_COOKIE);
            urlBuilder.append("?").append("userid=").append(userId)
                    .append("&appid=").append(SHPPUrlConstant.APP_ID)
                    .append("&ct=").append(ct)
                    .append("&code=").append(code)
                    .append("&ru=").append(ru);
            result.setDefaultModel("url",urlBuilder.toString());
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("buildCreateCookieUrl error userid:"+createCookieUrlApiParams.getUserid()+" ru="+createCookieUrlApiParams.getRu(),e);
        }
        return result;
    }

}
