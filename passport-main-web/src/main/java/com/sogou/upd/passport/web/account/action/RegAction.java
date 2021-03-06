package com.sogou.upd.passport.web.account.action;

import com.google.common.base.Strings;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.DateAndNumTimesConstant;
import com.sogou.upd.passport.common.LoginConstant;
import com.sogou.upd.passport.common.lang.StringUtil;
import com.sogou.upd.passport.common.model.useroperationlog.UserOperationLog;
import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.parameter.AccountModuleEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.common.utils.PhoneUtil;
import com.sogou.upd.passport.common.utils.RedisUtils;
import com.sogou.upd.passport.common.utils.ServletUtil;
import com.sogou.upd.passport.manager.account.CommonManager;
import com.sogou.upd.passport.manager.account.CookieManager;
import com.sogou.upd.passport.manager.account.RegManager;
import com.sogou.upd.passport.manager.account.SecureManager;
import com.sogou.upd.passport.manager.api.account.RegisterApiManager;
import com.sogou.upd.passport.manager.api.account.form.BaseMoblieApiParams;
import com.sogou.upd.passport.manager.api.account.form.CookieApiParams;
import com.sogou.upd.passport.manager.api.account.form.ResendActiveMailParams;
import com.sogou.upd.passport.manager.app.ConfigureManager;
import com.sogou.upd.passport.manager.form.ActiveEmailParams;
import com.sogou.upd.passport.manager.form.WebRegisterParams;
import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.web.BaseController;
import com.sogou.upd.passport.web.ControllerHelper;
import com.sogou.upd.passport.web.UserOperationLogUtil;
import com.sogou.upd.passport.web.account.form.MoblieCodeParams;
import com.sogou.upd.passport.web.account.form.RegUserNameParams;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web注册 User: mayan Date: 13-6-7 Time: 下午5:48
 */
@Controller
@RequestMapping("/web")
public class RegAction extends BaseController {

    @Autowired
    private RegManager regManager;
    @Autowired
    private ConfigureManager configureManager;
    @Autowired
    private SecureManager secureManager;
    @Autowired
    private RegisterApiManager registerApiManager;
    @Autowired
    private CookieManager cookieManager;
    @Autowired
    private CommonManager commonManager;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 用户注册检查用户名是否存在
     */
    @RequestMapping(value = "/account/checkusername", method = RequestMethod.GET)
    @ResponseBody
    public String checkusername(HttpServletRequest request, RegUserNameParams checkParam)
            throws Exception {
        Result result = new APIResultSupport(false);
        int clientId = CommonConstant.SGPP_DEFAULT_CLIENTID;
        try {
            //参数验证
            String validateResult = ControllerHelper.validateParams(checkParam);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            String username = URLDecoder.decode(checkParam.getUsername(), "utf-8");
            String clientIdStr = checkParam.getClient_id();
            if (!Strings.isNullOrEmpty(clientIdStr)) {
                clientId = Integer.valueOf(clientIdStr);
            }
            if (regManager.isUserInExistBlackList(checkParam.getUsername(), getIp(request))) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
            } else {
                result = checkAccountNotExists(username, clientId);
                if (PhoneUtil.verifyPhoneNumberFormat(username) && ErrorUtil.ERR_CODE_ACCOUNT_REGED.equals(result.getCode())) {
                    result.setMessage("此手机号已注册或已绑定，请直接登录");
                }
            }
        } catch (Exception e) {
            logger.error("checkusername:Check Username Is Failed,Username is " + checkParam.getUsername(), e);
        } finally {
            UserOperationLog userOperationLog = new UserOperationLog(checkParam.getUsername(), request.getRequestURI(), String.valueOf(clientId), result.getCode(), getIp(request));
            String referer = request.getHeader("referer");
            userOperationLog.putOtherMessage("ref", referer);
            UserOperationLogUtil.log(userOperationLog);
        }
        return result.toString();
    }

    /**
     * web页面注册
     *
     * @param regParams 传入的参数
     */
    @RequestMapping(value = "/reguser", method = RequestMethod.POST)
    @ResponseBody
    public Object reguser(HttpServletRequest request, HttpServletResponse response, WebRegisterParams regParams, Model model)
            throws Exception {
        Result result = new APIResultSupport(false);
        String ip = null;
        String uuidName = null;
        String finalCode = null;
        String actualClientId = null;
        try {
            //参数验证
            String validateResult = ControllerHelper.validateParams(regParams);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            ip = getIp(request);
            //校验用户是否允许注册
            uuidName = ServletUtil.getCookie(request, "uuidName");
            result = regManager.checkRegInBlackList(ip, uuidName);
            if (!result.isSuccess()) {
                if (result.getCode().equals(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST)) {
                    finalCode = ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST;
                    result.setCode(ErrorUtil.ERR_CODE_REGISTER_UNUSUAL);
                    result.setMessage("注册失败");
                }
                return result.toString();
            }

            // TODO 为解决其他业务线跳转到 passport 页面注册时，sendsms 使用的 1120，但注册使用其他 client id 导致手机验证码不匹配，用户注册失败
            // 暂时将注册时的 client id 手动设置为 1120，使用户可以正常注册。待前端 js 修复后将此处改回
            actualClientId = regParams.getClient_id();
            regParams.setClient_id("1120");

            int clientId = Integer.valueOf(regParams.getClient_id());
            //todo 防止邮箱注册攻击，临时增加接口频次限制
            if (isSnapBlackList(clientId)) {
                finalCode = ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST;
                result.setCode(ErrorUtil.ERR_CODE_REGISTER_UNUSUAL);
                result.setMessage("注册失败");
                return result.toString();
            }

            result = regManager.webRegister(regParams, ip);
            if (result.isSuccess()) {
                //设置来源
                String ru = regParams.getRu();
                if (Strings.isNullOrEmpty(ru)) {
                    ru = CommonConstant.DEFAULT_INDEX_URL;
                }
                String passportId = (String) result.getModels().get("username");
                Boolean isSetCookie = (Boolean) result.getModels().get("isSetCookie");
                if (isSetCookie) {
                    //最初版本调用
//                    result = cookieManager.setCookie(response, passportId, clientId, ip, ru, -1);
                    //新重载的方法、增加昵称参数、以及判断种老cookie还是新cookie module替换
//                    result = cookieManager.setCookie(response, passportId, clientId, ip, ru, -1, StringUtils.EMPTY);
                    String sgid = "";
                    if (result.getModels().get(LoginConstant.COOKIE_SGID) != null) {
                        sgid = result.getModels().get(LoginConstant.COOKIE_SGID).toString();
                    }

                    CookieApiParams cookieApiParams = new CookieApiParams();
                    cookieApiParams.setUserid(passportId);
                    cookieApiParams.setClient_id(clientId);
                    cookieApiParams.setRu(ru);
                    cookieApiParams.setTrust(CookieApiParams.IS_ACTIVE);
                    cookieApiParams.setPersistentcookie(String.valueOf(1));
                    cookieApiParams.setIp(ip);
                    cookieApiParams.setUniqname(StringUtils.EMPTY);
                    cookieApiParams.setMaxAge(-1);
                    cookieApiParams.setSgid(sgid);
                    cookieApiParams.setCreateAndSet(CommonConstant.CREATE_COOKIE_AND_SET);

                    result = cookieManager.createCookie(response, cookieApiParams);
                }
                result.setDefaultModel(CommonConstant.RESPONSE_RU, ru);
            }
        } catch (Exception e) {
            logger.error("reguser:User Register Is Failed,Username is " + regParams.getUsername(), e);
        } finally {
            String logCode = !Strings.isNullOrEmpty(finalCode) ? finalCode : result.getCode();
            regManager.incRegTimes(ip, uuidName);
            String userId = (String) result.getModels().get("userid");
            if (!Strings.isNullOrEmpty(userId) && AccountDomainEnum.getAccountDomain(userId) != AccountDomainEnum.OTHER) {
                if (result.isSuccess()) {
                    // 非外域邮箱用户不用验证，直接注册成功后记录登录记录
                    int clientId = Integer.parseInt(actualClientId);
                    secureManager.logActionRecord(userId, clientId, AccountModuleEnum.LOGIN, ip, null);
                }
            }
            //用户注册log
            UserOperationLog userOperationLog = new UserOperationLog(regParams.getUsername(), request.getRequestURI(), actualClientId, logCode, getIp(request));
            String referer = request.getHeader("referer");
            userOperationLog.putOtherMessage("ref", referer);
            UserOperationLogUtil.log(userOperationLog);
        }
        return result.toString();
    }

    private boolean isSnapBlackList(int clientId) {
        if (clientId == 1014) {
            String key = "SP.CLIENTID:WEBREGISTER_LIMITED_NUM_" + clientId;
            try {
                if (redisUtils.checkKeyIsExist(key)) {
                    int checkNum = Integer.parseInt(redisUtils.get(key));
                    if (checkNum >= 1000) {// 1小时最多注册1000个
                        return true;
                    } else {
                        redisUtils.increment(key);
                    }
                } else {
                    redisUtils.setWithinSeconds(key, "1", DateAndNumTimesConstant.ONE_HOUR_INSECONDS);
                }
                return false;
            } catch (Exception e) {
                logger.error("[/web/reguser] reguser limit.", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 重新发送激活邮件
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/resendActiveMail", method = RequestMethod.POST)
    @ResponseBody
    public Object resendActiveMail(HttpServletRequest request, ResendActiveMailParams params) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            //参数验证
            String validateResult = ControllerHelper.validateParams(params);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            //检查client_id是否存在
            int clientId = Integer.parseInt(params.getClient_id());
            if (!configureManager.checkAppIsExist(clientId)) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                return result;
            }
            String username = params.getUsername();
            //如果账号存在并且状态为未激活，则重新发送激活邮件
            Account account = commonManager.queryAccountByPassportIdInCache(username);
            if (account != null) {
                switch (account.getFlag()) {
                    case 0:
                        //未激活，发送激活邮件
                        result = regManager.resendActiveMail(params);
                        break;
                    case 1:
                        //正式用户，可直接登录
                        result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
                        break;
                    case 2:
                        //用户已经被封杀
                        result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_KILLED);
                        break;
                }
            } else {
                result.setCode(ErrorUtil.INVALID_ACCOUNT);
            }
        } catch (Exception e) {
            logger.error("method[resendActiveMail] send mobile sms error.{}", e);
        } finally {
            UserOperationLog userOperationLog = new UserOperationLog(params.getUsername(), request.getRequestURI(), params.getClient_id(), result.getCode(), getIp(request));
            String referer = request.getHeader("referer");
            userOperationLog.putOtherMessage("ref", referer);
            UserOperationLogUtil.log(userOperationLog);
        }
        return result.toString();
    }

    /**
     * 邮件激活
     *
     * @param activeParams 传入的参数
     */
    @RequestMapping(value = "/activemail", method = RequestMethod.GET)
    public void activeEmail(HttpServletRequest request, HttpServletResponse response, ActiveEmailParams activeParams, Model model)
            throws Exception {
        Result result;
        //参数验证
        String validateResult = ControllerHelper.validateParams(activeParams);
        if (!Strings.isNullOrEmpty(validateResult)) {
            response.sendRedirect(CommonConstant.EMAIL_REG_VERIFY_URL + "?code=" + ErrorUtil.ERR_CODE_COM_REQURIE + "&client_id=" + activeParams.getClient_id());
            return;
        }
        //验证client_id
        int clientId = Integer.parseInt(activeParams.getClient_id());
        //检查client_id是否存在
        if (!configureManager.checkAppIsExist(clientId)) {
            response.sendRedirect(CommonConstant.EMAIL_REG_VERIFY_URL + "?code=" + ErrorUtil.INVALID_CLIENTID + "&client_id=" + activeParams.getClient_id());
            return;
        }
        String ip = getIp(request);
        //邮件激活
        result = regManager.activeEmail(activeParams, ip);
        if (result.isSuccess()) {
            //最初版本
//            result = cookieManager.setCookie(response, activeParams.getPassport_id(), clientId, ip, activeParams.getRu(), -1);
            //新重载的方法、增加昵称参数、以及判断种老cookie还是新cookie module 替换
//            result = cookieManager.setCookie(response, activeParams.getPassport_id(), clientId, ip, activeParams.getRu(), -1, StringUtils.EMPTY);

            String sgid = "";
            if (result.getModels().get(LoginConstant.COOKIE_SGID) != null) {
                sgid = result.getModels().get(LoginConstant.COOKIE_SGID).toString();
            }

            CookieApiParams cookieApiParams = new CookieApiParams();
            cookieApiParams.setUserid(activeParams.getPassport_id());
            cookieApiParams.setClient_id(clientId);
            cookieApiParams.setRu(activeParams.getRu());
            cookieApiParams.setTrust(CookieApiParams.IS_ACTIVE);
            cookieApiParams.setPersistentcookie(String.valueOf(1));
            cookieApiParams.setIp(ip);
            cookieApiParams.setUniqname(StringUtils.EMPTY);
            cookieApiParams.setMaxAge(-1);
            cookieApiParams.setSgid(sgid);
            cookieApiParams.setCreateAndSet(CommonConstant.CREATE_COOKIE_AND_SET);

            result = cookieManager.createCookie(response, cookieApiParams);
            if (result.isSuccess()) {
                String ru = activeParams.getRu();
                if (Strings.isNullOrEmpty(ru) || CommonConstant.EMAIL_REG_VERIFY_URL.equals(ru)) {
                    ru = CommonConstant.DEFAULT_INDEX_URL;
                }

                if(activeParams.isRtp()) { // 跳转到 passport 页面
                    response.sendRedirect(CommonConstant.EMAIL_REG_VERIFY_URL + "?code=0&ru=" + ru + "&client_id=" + clientId);
                    return ;
                } else {
                    response.sendRedirect(activeParams.getRu() + "?code=0&client_id=" + clientId
                                          + "&userId=" + activeParams.getPassport_id());
                    return ;
                }
            }
        }

        if(activeParams.isRtp()) { // 跳转到 passport 页面
            response.sendRedirect(CommonConstant.EMAIL_REG_VERIFY_URL + "?code=" + result.getCode() + "&client_id=" + clientId);
        } else {
            response.sendRedirect(activeParams.getRu() + "?code=" + result.getCode() + "&client_id=" + clientId
                                  + "&userId=" + activeParams.getPassport_id());
        }
    }

    /**
     * web页面手机账号注册时发送的验证码
     *
     * @param reqParams 传入的参数
     */
    @RequestMapping(value = {"/sendsms"}/*, method = RequestMethod.POST*/)
    @ResponseBody
    public Object sendMobileCode(MoblieCodeParams reqParams, HttpServletRequest request)
            throws Exception {
        //sendsms因SDK已经发版，需要兼容， 改个策略，
        // GET方式中，如果有cinfo参数，并且参数值内容匹配，这样能发验证码，其它的还是POST限制。
        boolean canProcessGet = false;
        String cInfo;
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            cInfo = request.getHeader(CommonConstant.MAPP_REQUEST_HEADER_SIGN);
            if (StringUtil.isNotEmpty(cInfo)) {
                canProcessGet = true;
            }
        } else if (!"POST".equalsIgnoreCase(request.getMethod()) && !canProcessGet) {
            throw new Exception("can't process GET");
        }
        ////////end //////////////////
        Result result = new APIResultSupport(false);
        String finalCode = null;
        String ip = getIp(request);
        try {
            //参数验证
            String validateResult = ControllerHelper.validateParams(reqParams);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            //验证client_id
            int clientId = Integer.parseInt(reqParams.getClient_id());
            //检查client_id是否存在
            if (!configureManager.checkAppIsExist(clientId)) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                return result.toString();
            }
            String mobile = reqParams.getMobile();
            String userAgent = request.getHeader(CommonConstant.USER_AGENT);
            cInfo = request.getHeader(CommonConstant.MAPP_REQUEST_HEADER_SIGN);
            boolean isNeedCaptcha = false;
            //只有客户端才会有此"cinfo"参数，web端和桌面端是没有的，故客户端和手机端还走第二次弹出验证码的流程
            if (!Strings.isNullOrEmpty(cInfo) || (!Strings.isNullOrEmpty(userAgent) && (userAgent.toLowerCase().contains("android") || userAgent.toLowerCase().contains("iphone")))) {
                if (Integer.parseInt(reqParams.getClient_id()) == 2003 && "GET".equalsIgnoreCase(request.getMethod())) {
                    isNeedCaptcha = false;      // 输入法安卓版本使用passport老版本sdk，/sendsms仍是get方式，且不支持弹出图片验证码
                } else {
                    result = commonManager.checkMobileSendSMSInBlackList(mobile, reqParams.getClient_id());
                    if (!result.isSuccess() && ErrorUtil.ERR_CODE_ACCOUNT_CAPTCHA_NEED_CODE.equals(result.getCode())) {
                        isNeedCaptcha = true;
                    }
                }
            } else {
                if (CommonConstant.PC_CLIENTID != Integer.parseInt(reqParams.getClient_id())) {
                    isNeedCaptcha = true;
                }
            }
            if (isNeedCaptcha) {
                if (!Strings.isNullOrEmpty(reqParams.getToken()) && !Strings.isNullOrEmpty(reqParams.getCaptcha())) {
                    result = regManager.checkCaptchaToken(reqParams.getToken(), reqParams.getCaptcha());
                    //如果验证码校验失败，则提示
                    if (!result.isSuccess()) {
                        result.setDefaultModel("token", RandomStringUtils.randomAlphanumeric(48));
                        result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CAPTCHA_CODE_FAILED);
                        return result.toString();
                    }
                } else {
                    //桌面端需要兼容浏览器1044不弹出验证码的情况
                    result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CAPTCHA_NEED_CODE);
                    result.setDefaultModel("token", RandomStringUtils.randomAlphanumeric(48));
                    return result.toString();
                }
            }
            //校验用户ip是否中了黑名单
            result = commonManager.checkMobileSendSMSInBlackList(ip, reqParams.getClient_id());
            if (!result.isSuccess()) {
                finalCode = ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST;
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_SMSCODE_SEND);
                result.setMessage("发送短信失败");
                return result.toString();
            }
            result = registerApiManager.sendMobileRegCaptcha(clientId, mobile);
        } catch (Exception e) {
            logger.error("method[sendMobileCode] send mobile sms error.{}", e);
        } finally {
            String logCode;
            if (!Strings.isNullOrEmpty(finalCode)) {
                logCode = finalCode;
            } else {
                logCode = result.getCode();
            }
            //web页面手机注册时，发送手机验证码
            UserOperationLog userOperationLog = new UserOperationLog(reqParams.getMobile(), request.getRequestURI(), reqParams.getClient_id(), logCode, ip);
            String referer = request.getHeader("referer");
            userOperationLog.putOtherMessage("ref", referer);
            UserOperationLogUtil.log(userOperationLog);
        }

        commonManager.incSendTimesForMobile(ip);
        commonManager.incSendTimesForMobile(reqParams.getMobile());
        return result.toString();
    }

    private BaseMoblieApiParams buildProxyApiParams(int clientId, String mobile) {
        BaseMoblieApiParams baseMoblieApiParams = new BaseMoblieApiParams();
        baseMoblieApiParams.setMobile(mobile);
        baseMoblieApiParams.setClient_id(clientId);
        return baseMoblieApiParams;
    }

    //检查用户是否存在
    protected Result checkAccountNotExists(String username, int clientId) throws Exception {
        Result result = new APIResultSupport(false);
        //校验是否是搜狐域内用户
        if (AccountDomainEnum.SOHU.equals(AccountDomainEnum.getAccountDomain(username))) {
            result.setCode(ErrorUtil.ERR_CODE_NOTSUPPORT_SOHU_REGISTER);
            return result;
        }
        //校验是否是搜狗用户
        if (AccountDomainEnum.SOGOU.equals(AccountDomainEnum.getAccountDomain(username))) {
            result.setCode(ErrorUtil.ERR_CODE_NOTSUPPORT_SOGOU_REGISTER);
            return result;
        }
        //检查用户名是否存在
        result = registerApiManager.checkUser(username, clientId,false);
        return result;
    }

    /*
     外域邮箱用户激活成功的页面
   */
    @RequestMapping(value = "/reg/emailverify", method = RequestMethod.GET)
    public String emailVerifySuccess(String code, String ru, Model model) throws Exception {
        Result result = new APIResultSupport(false);
        if ("0".equals(code)) {
            result.setSuccess(true);
            result.setDefaultModel("ru", ru);
        } else {
            result.setCode(code);
        }
        model.addAttribute("data", result.toString());
        //状态码参数
        return "reg/emailsuccess";
    }
}
