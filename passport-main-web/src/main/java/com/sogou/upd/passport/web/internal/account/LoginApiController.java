package com.sogou.upd.passport.web.internal.account;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.model.useroperationlog.UserOperationLog;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.manager.account.CookieManager;
import com.sogou.upd.passport.manager.account.LoginManager;
import com.sogou.upd.passport.manager.account.PCAccountManager;
import com.sogou.upd.passport.manager.api.account.LoginApiManager;
import com.sogou.upd.passport.manager.api.account.form.AppAuthTokenApiParams;
import com.sogou.upd.passport.manager.api.account.form.AuthUserApiParams;
import com.sogou.upd.passport.manager.api.account.form.CookieApiParams;
import com.sogou.upd.passport.manager.api.account.form.ReNewCookieApiParams;
import com.sogou.upd.passport.manager.app.ConfigureManager;
import com.sogou.upd.passport.web.BaseController;
import com.sogou.upd.passport.web.ControllerHelper;
import com.sogou.upd.passport.web.UserOperationLogUtil;
import com.sogou.upd.passport.web.annotation.InterfaceSecurity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Web登录的内部接口
 * User: shipengzhi
 * Date: 13-6-6
 * Time: 下午2:40
 */
@Controller
@RequestMapping("/internal")
public class LoginApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);
    @Autowired
    private PCAccountManager pcAccountManager;
    @Autowired
    private LoginManager loginManager;
    @Autowired
    private LoginApiManager loginApiManager;
    @Autowired
    private ConfigureManager configureManager;
    @Autowired
    private CookieManager cookieManager;

    /**
     * 续种cookie
     *
     * @param request
     * @param params
     * @return
     */
    @InterfaceSecurity
    @RequestMapping(value = "/account/renewcookie")
    @ResponseBody
    public Object renewcookie(HttpServletRequest request, HttpServletResponse response, ReNewCookieApiParams params) {
        Result result = new APIResultSupport(false);
        // 参数校验
        String validateResult = ControllerHelper.validateParams(params);
        if (!Strings.isNullOrEmpty(validateResult)) {
            result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
            result.setMessage(validateResult);
            return result.toString();
        }
        //验证client_id是否存在
        int clientId = params.getClient_id();
        if (!configureManager.checkAppIsExist(clientId)) {
            result.setCode(ErrorUtil.INVALID_CLIENTID);
            return result.toString();
        }
        //todo 检查用户名是否存在
        //设置来源
        String ru = params.getRu();
        if (Strings.isNullOrEmpty(ru)) {
            ru = CommonConstant.DEFAULT_INDEX_URL;
        }
        String passportId = params.getUserid();
        String ip = getIp(request);
//        CookieApiParams cookieApiParams = new CookieApiParams(passportId, clientId, ru, ip);
//        Result getCookieValueResult = proxyLoginApiManager.getCookieInfo(cookieApiParams);

        CookieApiParams cookieApiParams = new CookieApiParams();
        cookieApiParams.setUserid(passportId);
        cookieApiParams.setClient_id(clientId);
        cookieApiParams.setRu(ru);
        cookieApiParams.setTrust(CookieApiParams.IS_ACTIVE);
        cookieApiParams.setPersistentcookie(String.valueOf(1));
        cookieApiParams.setIp(ip);

        if (!Strings.isNullOrEmpty(passportId)) {
            if (StringUtils.contains(passportId, "@")) {
                cookieApiParams.setUniqname(StringUtils.substring(passportId, 0, passportId.indexOf("@")));
            } else {
                cookieApiParams.setUniqname(passportId);
            }
        }

        cookieApiParams.setCreateAndSet(CommonConstant.CREATE_COOKIE_NOT_SET);
        Result getCookieValueResult = cookieManager.createCookie(response, cookieApiParams);

        if (getCookieValueResult.isSuccess()) {
            String ppinf = (String) getCookieValueResult.getModels().get("ppinf");
            String pprdig = (String) getCookieValueResult.getModels().get("pprdig");
            result.setSuccess(true);
            Map<String, String> map = Maps.newHashMap();
            map.put("userid", passportId);
            map.put("ppinf", ppinf);
            map.put("pprdig", pprdig);
            result.setModels(map);
        } else {
            result.setCode(ErrorUtil.ERR_CODE_CREATE_COOKIE_FAILED);
        }
        // 获取记录UserOperationLog的数据
        UserOperationLog userOperationLog = new UserOperationLog(params.getUserid(), String.valueOf(params.getClient_id()), result.getCode(), getIp(request));
        UserOperationLogUtil.log(userOperationLog);
        return result.toString();
    }

    /**
     * pop3校验用户名和密码是否正确
     *
     * @param request
     * @param params
     * @return
     */
    @InterfaceSecurity
    @RequestMapping(value = "/account/authemailuser", method = RequestMethod.POST)
    @ResponseBody
    public Object authEmailUser(HttpServletRequest request, final AuthUserApiParams params) {
        Result result = new APIResultSupport(false);
        // 参数校验
        String validateResult = ControllerHelper.validateParams(params);
        if (!Strings.isNullOrEmpty(validateResult)) {
            result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
            result.setMessage(validateResult);
            return result.toString();
        }
        String createip = params.getCreateip();
        String userid = params.getUserid();
        try {
            if (pcAccountManager.isLoginUserInBlackList(params.getClient_id(), userid, createip)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result.toString();
            }
            result = loginApiManager.webAuthUser(params);
            if (!result.isSuccess()) {
                pcAccountManager.doAuthUserFailed(params.getClient_id(), userid, createip, result.getCode());
            }
        } catch (Exception e) {
            logger.error("/internal/account/authemailuser failed,passportId:" + userid, e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result.toString();
        } finally {
            UserOperationLog userOperationLog = new UserOperationLog(userid, String.valueOf(params.getClient_id()), result.getCode(), getIp(request));
            userOperationLog.putOtherMessage("createip", createip);
            UserOperationLogUtil.log(userOperationLog);
            return result.toString();
        }
    }

    /**
     * web端校验用户名和密码是否正确
     *
     * @param request
     * @param params
     * @return
     */
    @InterfaceSecurity
    @RequestMapping(value = "/account/authuser", method = RequestMethod.POST)
    @ResponseBody
    public Object webAuthUser(HttpServletRequest request, AuthUserApiParams params) {
        Result result = new APIResultSupport(false);
        // 参数校验
        String validateResult = ControllerHelper.validateParams(params);
        if (!Strings.isNullOrEmpty(validateResult)) {
            result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
            result.setMessage(validateResult);
            return result.toString();
        }
        String createip = params.getCreateip();
        try {
            if (StringUtils.isEmpty(createip)) {
                createip = null;
            }
            if (loginManager.isLoginUserInBlackList(params.getUserid(), createip)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result.toString();
            }
            // 调用内部接口
            result = loginApiManager.webAuthUser(params);
            if (result.isSuccess()) {
                String userId = result.getModels().get("userid").toString();
                loginManager.doAfterLoginSuccess(params.getUserid(), createip, userId, params.getClient_id());
            } else {
                loginManager.doAfterLoginFailed(params.getUserid(), createip, result.getCode());
                result.setMessage("用户名或密码错误");
            }
        } catch (Exception e) {
            logger.error("authuser fail,userid:" + params.getUserid(), e);
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_LOGIN_FAILED);
            return result.toString();
        } finally {
            // 获取记录UserOperationLog的数据
            UserOperationLog userOperationLog = new UserOperationLog(params.getUserid(), String.valueOf(params.getClient_id()), result.getCode(), getIp(request));
            userOperationLog.putOtherMessage("createip", createip);
            UserOperationLogUtil.log(userOperationLog);
            return result.toString();
        }
    }

    /**
     * 手机应用使用第三方登录完成之后，会通过302重定向的方式将token带给产品的服务器端，
     * 产品的服务器端通过传入userid和token验证用户的合法性，且token具有较长的有效期。
     * 注意，目前接入应用全部是验证token，没有传入userid
     * todo 以后可考虑，验证token时，直接返回用户信息；现阶段为防止获取用户信息失败
     * 而导致验证登录状态验证失败，暂时不做
     *
     * @return
     */
    @InterfaceSecurity
    @RequestMapping(value = "/account/authtoken", method = RequestMethod.POST)
    @ResponseBody
    public Object appAuthToken(HttpServletRequest request, AppAuthTokenApiParams params) {
        Result result = new APIResultSupport(false);
        // 参数校验
        String validateResult = ControllerHelper.validateParams(params);
        if (!Strings.isNullOrEmpty(validateResult)) {
            result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
            result.setMessage(validateResult);
            return result.toString();
        }
        // 调用内部接口
        result = loginApiManager.appAuthToken(params.getToken());
        String userId = (String) result.getModels().get("userid");
        //记录log
        UserOperationLog userOperationLog = new UserOperationLog(StringUtils.defaultIfEmpty(userId, "third"), String.valueOf(params.getClient_id()), result.getCode(), getIp(request));
        UserOperationLogUtil.log(userOperationLog);

        return result.toString();
    }
}
