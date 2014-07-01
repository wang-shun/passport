package com.sogou.upd.passport.manager.account.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.lang.StringUtil;
import com.sogou.upd.passport.common.math.Coder;
import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.parameter.AccountModuleEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.*;
import com.sogou.upd.passport.exception.ServiceException;
import com.sogou.upd.passport.manager.ManagerHelper;
import com.sogou.upd.passport.manager.account.SecureManager;
import com.sogou.upd.passport.manager.account.vo.AccountSecureInfoVO;
import com.sogou.upd.passport.manager.account.vo.ActionRecordVO;
import com.sogou.upd.passport.manager.api.account.BindApiManager;
import com.sogou.upd.passport.manager.api.account.LoginApiManager;
import com.sogou.upd.passport.manager.api.account.SecureApiManager;
import com.sogou.upd.passport.manager.api.account.UserInfoApiManager;
import com.sogou.upd.passport.manager.api.account.form.*;
import com.sogou.upd.passport.manager.form.UpdatePwdParameters;
import com.sogou.upd.passport.manager.form.UserNamePwdMappingParams;
import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.model.account.ActionRecord;
import com.sogou.upd.passport.model.app.AppConfig;
import com.sogou.upd.passport.service.account.*;
import com.sogou.upd.passport.service.account.dataobject.ActionStoreRecordDO;
import com.sogou.upd.passport.service.app.AppConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 安全相关：修改密码、修改密保（手机、邮箱、问题） ——接口代理OK—— .
 */
@Component
public class SecureManagerImpl implements SecureManager {

    private static Logger logger = LoggerFactory.getLogger(SecureManagerImpl.class);

    private static Logger profileErrorLogger = LoggerFactory.getLogger("profileErrorLogger");

    private static String SECURE_FIELDS = "sec_email,sec_mobile,sec_ques";

    //搜狗安全信息字段:密保邮箱、密保手机、密保问题
    private static final String SOGOU_SECURE_FIELDS = "email,mobile,question,uniqname,avatarurl";

    @Autowired
    private MobileCodeSenderService mobileCodeSenderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private MobilePassportMappingService mobilePassportMappingService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AccountSecureService accountSecureService;
    @Autowired
    private OperateTimesService operateTimesService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private BindApiManager bindApiManager;
    @Autowired
    private LoginApiManager loginApiManager;
    @Autowired
    private SecureApiManager secureApiManager;
    @Autowired
    private SecureApiManager sgSecureApiManager;
    @Autowired
    private UserInfoApiManager proxyUserInfoApiManager;
    @Autowired
    private BindApiManager sgBindApiManager;
    @Autowired
    private BindApiManager proxyBindApiManager;
    @Autowired
    private UserInfoApiManager sgUserInfoApiManager;
    @Autowired
    private CommonManagerImpl commonManager;
    @Autowired
    private PhotoUtils photoUtils;

    private ExecutorService service = Executors.newFixedThreadPool(10);

    private static final String PREFIX_UPDATE_PWD_SEND_MESSAGE = "搜狗通行证提醒您：您的账号没有注册或绑定手机号：";
    private static final String SUFFIX_UPDATE_PWD_SEND_MESSAGE = "重置密码失败!";

    /*
     * 发送短信至未绑定手机，只检测映射表，查询passportId不存在或为空即认定为未绑定
     */
    @Override
    public Result sendMobileCode(String mobile, int clientId, AccountModuleEnum module) {
        Result result = new APIResultSupport(false);
        try {
            //校验手机号格式
            if (Strings.isNullOrEmpty(mobile) || !PhoneUtil.verifyPhoneNumberFormat(mobile)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONEERROR);
                return result;
            }
            // 验证码验证错误次数是否小于限制次数,一天不超过10次
            boolean checkFailLimited = mobileCodeSenderService.checkLimitForSmsFail(mobile, clientId, module);
            if (!checkFailLimited) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKSMSCODE_LIMIT);
                return result;
            }
            result = mobileCodeSenderService.sendSmsCode(mobile, clientId, module);
            return result;
        } catch (ServiceException e) {
            logger.error("send sms code to mobile Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }


    @Override
    public Result sendMobileCodeByPassportId(String passportId, int clientId, AccountModuleEnum module)
            throws Exception {
        Result result = new APIResultSupport(false);
        try {
            Account account = accountService.queryNormalAccount(passportId);
            if (account == null) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_NOTHASACCOUNT);
                return result;
            }
            String mobile = account.getMobile();
            if (Strings.isNullOrEmpty(mobile)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONE_OBTAIN_FIELDS);
                return result;
            }
            return sendMobileCode(mobile, clientId, module);
        } catch (ServiceException e) {
            logger.error("send mobile code Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    @Override
    public Result sendMobileCodeOld(String userId, int clientId) {
        Result result = new APIResultSupport(false);
        try {
            result = sendMobileCodeByPassportId(userId, clientId, AccountModuleEnum.SECURE);
            if (!result.isSuccess()) {
                return result;
            }
            result.setMessage("密保手机验证码发送成功！");
            return result;
        } catch (Exception e) {
            logger.error("send mobile code old Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    @Override
    public Result queryAccountSecureInfo(String userId, int clientId, boolean doProcess) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            AppConfig appConfig = appConfigService.queryAppConfigByClientId(clientId);
            if (appConfig == null) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                return result;
            }

            int score = 0; // 安全系数
            AccountSecureInfoVO accountSecureInfoVO = new AccountSecureInfoVO();

            //TODO 去掉开关
//            if (ManagerHelper.isInvokeProxyApi(userId)) {
//                // 代理接口
//                GetUserInfoApiparams getUserInfoApiparams = new GetUserInfoApiparams();
//                getUserInfoApiparams.setUserid(userId);
//                getUserInfoApiparams.setClient_id(clientId);
//                getUserInfoApiparams.setImagesize("50");
//                getUserInfoApiparams.setFields(SECURE_FIELDS);
//
//                //调用sohu 接口取用户信息
//                result = proxyUserInfoApiManager.getUserInfo(getUserInfoApiparams);
//
//                Result shPlusResult = shPlusUserInfoApiManager.getUserInfo(getUserInfoApiparams);
//                if (shPlusResult.isSuccess()) {
//                    Object obj = shPlusResult.getModels().get("baseInfo");
//                    if (obj != null) {
//                        AccountBaseInfo baseInfo = (AccountBaseInfo) obj;
//                        String uniqname = baseInfo.getUniqname();
//                        result.getModels().put("uniqname", Coder.encode(Strings.isNullOrEmpty(uniqname) ? userId : uniqname, "UTF-8"));
//                        Result photoResult = photoUtils.obtainPhoto(baseInfo.getAvatar(), "50");
//                        if (photoResult.isSuccess()) {
//                            result.getModels().put("avatarurl", photoResult.getModels());
//                        }
//                    } else {
//                        result.getModels().put("uniqname", userId);
//                    }
//                }
//            } else {

            //TODO 统一调用 AccountInfoManager getUserInfo 方法

//                GetSecureInfoApiParams params = new GetSecureInfoApiParams();
//                params.setUserid(userId);
//                params.setClient_id(clientId);
//                result = sgSecureApiManager.getUserSecureInfo(params);


            //调用 SGUserInfoApiManagerImpl 中 getUserInfo

            GetUserInfoApiparams getUserInfoApiparams = new GetUserInfoApiparams();
            getUserInfoApiparams.setUserid(userId);
            getUserInfoApiparams.setClient_id(clientId);
            getUserInfoApiparams.setFields(SOGOU_SECURE_FIELDS);

            AccountDomainEnum domain = AccountDomainEnum.getAccountDomain(userId);
            if (domain == AccountDomainEnum.THIRD) {
                result = sgUserInfoApiManager.getUserInfo(getUserInfoApiparams);
            } else {
                result = sgUserInfoApiManager.getUserInfo(getUserInfoApiparams);
                if (!result.isSuccess()) {
                    result = proxyUserInfoApiManager.getUserInfo(getUserInfoApiparams);
                    if (result.isSuccess()) {
                        //记录Log 跟踪数据同步延时情况
                        LogUtil.buildErrorLog(profileErrorLogger, AccountModuleEnum.USERINFO, "getuserinfo", CommonConstant.CHECK_SGN_SHY_MESSAGE, userId, userId, result.toString());
                    }

                    result.getModels().put("uniqname", defaultUniqname(userId));
                    result.getModels().put("avatarurl", StringUtils.EMPTY);
                }
                String uniqname = String.valueOf(result.getModels().get("uniqname"));
                result.getModels().put("uniqname", Coder.encode(Strings.isNullOrEmpty(uniqname) ? userId : uniqname, "UTF-8"));
                Result photoResult = photoUtils.obtainPhoto(String.valueOf(result.getModels().get("avatarurl")), "50");
                if (photoResult.isSuccess()) {
                    result.getModels().put("avatarurl", photoResult.getModels());
                }
            }
//            }

            Map<String, String> map = result.getModels();
            result.setModels(map);

            if (!result.isSuccess()) {
                return result;
            }

            String mobile = map.get("sec_mobile");
            String emailBind = map.get("sec_email");
            String question = map.get("sec_ques");

            if (doProcess) {
                if (!Strings.isNullOrEmpty(emailBind)) {
                    String emailProcessed = StringUtil.processEmail(emailBind);
                    accountSecureInfoVO.setSec_email(emailProcessed);
                    score += 30;
                }
                if (!Strings.isNullOrEmpty(mobile)) {
                    String mobileProcessed = StringUtil.processMobile(mobile);
                    accountSecureInfoVO.setSec_mobile(mobileProcessed);
                    score += 30;
                }
                if (!Strings.isNullOrEmpty(question)) {
                    accountSecureInfoVO.setSec_ques(question);
                    score += 30;
                }
                if (AccountDomainEnum.getAccountDomain(userId) == AccountDomainEnum.OTHER) {
                    String emailRegProcessed = StringUtil.processEmail(userId);
                    accountSecureInfoVO.setReg_email(emailRegProcessed);
                }
            } else {
                if (!Strings.isNullOrEmpty(emailBind)) {
                    accountSecureInfoVO.setSec_email(emailBind);
                    score += 30;
                }
                if (!Strings.isNullOrEmpty(mobile)) {
                    accountSecureInfoVO.setSec_mobile(mobile);
                    score += 30;
                }
                if (!Strings.isNullOrEmpty(question)) {
                    accountSecureInfoVO.setSec_ques(question);
                    score += 30;
                }
                if (AccountDomainEnum.getAccountDomain(userId) == AccountDomainEnum.OTHER) {
                    accountSecureInfoVO.setReg_email(userId);
                }
            }
            accountSecureInfoVO.setSec_score(score);
            ActionRecordVO record = queryLastActionRecordPrivate(userId, clientId, AccountModuleEnum.LOGIN);
            if (record != null) {
                accountSecureInfoVO.setLast_login_time(record.getTime());
                accountSecureInfoVO.setLast_login_loc(record.getLoc());
            }

            result.setSuccess(true);
            result.setMessage("查询成功");
            result.setDefaultModel(accountSecureInfoVO);
            return result;
        } catch (ServiceException e) {
            logger.error("query account_secure_info Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    @Override
    public Result updateWebPwd(UpdatePwdParameters updatePwdParameters)
            throws Exception {
        Result result = new APIResultSupport(false);
        String passportId = null;
        try {
            passportId = updatePwdParameters.getPassport_id();
            String captcha = updatePwdParameters.getCaptcha();
            String modifyIp = updatePwdParameters.getIp();
            int clientId = Integer.parseInt(updatePwdParameters.getClient_id());
            String token = updatePwdParameters.getToken();
            //修改密码时检查验证码、ip黑白名单、修改密码次数
            result = checkUpdatePwdCaptchaAndSecure(passportId, clientId, token, captcha, modifyIp);
            if (result.isSuccess()) {
                result = secureApiManager.updatePwd(passportId, clientId, updatePwdParameters.getPassword(), updatePwdParameters.getNewPwd(), modifyIp);
                //TODO 所有账号只写SG库时此判断即可去掉；因SG账号只写先上，所以SG账号写分离时不需要再记此标记了
                if (!ManagerHelper.readSohuSwitcher() && result.isSuccess()) {
                    accountSecureService.updateSuccessFlag(passportId);
                }
                if (result.isSuccess()) {
                    operateTimesService.incLimitResetPwd(passportId, clientId);
                    operateTimesService.incResetPwdIPTimes(modifyIp);
                }
            }
        } catch (ServiceException e) {
            logger.error("Modify Web Password Fail username:" + passportId, e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
        return result;
    }

    private Result checkUpdatePwdCaptchaAndSecure(String passportId, int clientId, String token, String captcha, String modifyIp) {
        Result result = new APIResultSupport(true);
        try {
            if (!accountService.checkCaptchaCode(token, captcha)) {    //判断验证码
                logger.debug("[webRegister captchaCode wrong warn]:passportId=" + passportId + ", modifyIp=" + modifyIp + ", token=" + token + ", captchaCode=" + captcha);
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CAPTCHA_CODE_FAILED);
                return result;
            }

            if (operateTimesService.checkIPLimitResetPwd(modifyIp)) {    //检查是否在ip黑名单里
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result;
            }

            if (operateTimesService.checkLimitResetPwd(passportId, clientId)) {   //检查修改密码次数是否超限
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_RESETPASSWORD_LIMITED);
                return result;
            }
        } catch (ServiceException e) {
            logger.error("UpdatePwd Captcha Or Secure Fail, passportId:" + passportId, e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
        return result;
    }

    @Override
    public void resetPwd(List<UserNamePwdMappingParams> list) throws Exception {

        if (CollectionUtils.isNotEmpty(list)) {
            for (final UserNamePwdMappingParams params : list) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        String username = params.getUsername();
                        try {
                            //查是否是手机号码
                            if (PhoneUtil.verifyPhoneNumberFormat(username)) {
                                //查是否进黑名单
                                if (operateTimesService.checkLimitResetPwd(username + "@sohu.com", 0)) {
                                    //查sogou是否注册或绑定此手机号
                                    String passportId = mobilePassportMappingService.queryPassportIdByUsername(username);
                                    if (!Strings.isNullOrEmpty(passportId)) {
                                        UpdatePwdApiParams updatePwdApiParams = new UpdatePwdApiParams();
                                        updatePwdApiParams.setUserid(username);
                                        updatePwdApiParams.setNewpassword(params.getPwd());
                                        //校验是否是搜狗用户
                                        sgSecureApiManager.resetPwd(updatePwdApiParams);
                                    } else {
                                        //短信通知没有注册或绑定过此账号
                                        String smsText = PREFIX_UPDATE_PWD_SEND_MESSAGE + username + SUFFIX_UPDATE_PWD_SEND_MESSAGE;
                                        if (!Strings.isNullOrEmpty(username)) {
                                            SMSUtil.sendSMS(username, smsText);
                                        }
                                    }
                                    operateTimesService.incLimitResetPwd(username + "@sogou.com", 0);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("resetPwd Fail username:" + username, e);
                        }
                    }
                });
            }
        }
    }

    /* --------------------------------------------修改密保内容-------------------------------------------- */
    /*
     * 修改密保邮箱——1.验证原绑定邮箱及发送邮件至待绑定邮箱
     */
    @Override
    public Result sendEmailForBinding(String passportId, int clientId, String password,
                                      String newEmail, String oldEmail, String modifyIp, String ru) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            if (operateTimesService.checkIPBindLimit(modifyIp)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result;
            }

            if (!operateTimesService.checkBindLimit(passportId, clientId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDNUM_LIMITED);
                return result;
            }
            if (!operateTimesService.checkLimitCheckPwdFail(passportId, clientId, AccountModuleEnum.SECURE)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKPWDFAIL_LIMIT);
                return result;
            }
            if (!emailSenderService.checkLimitForSendEmail(passportId, clientId, AccountModuleEnum.SECURE, newEmail)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_SENDEMAIL_LIMITED);
                return result;
            }
            BindEmailApiParams params = new BindEmailApiParams();
            params.setUserid(passportId);
            params.setClient_id(clientId);
            params.setPassword(password);
            params.setNewbindemail(newEmail);
            params.setOldbindemail(oldEmail);

            String flag = String.valueOf(System.currentTimeMillis());
            ru = ru + "?token=" + accountSecureService.getSecureCodeRandom(flag) + "&id=" + flag;
            params.setRu(ru);

            result = bindApiManager.bindEmail(params);
            if (result.isSuccess()) {
                emailSenderService.incLimitForSendEmail(passportId, clientId, AccountModuleEnum.SECURE, newEmail);
                operateTimesService.incIPBindTimes(modifyIp);
                result.setMessage("发送绑定邮箱申请邮件成功！");
            } else if (ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_PWD_ERROR.equals(result.getCode())) {
                operateTimesService.incLimitCheckPwdFail(passportId, clientId, AccountModuleEnum.SECURE);
            }
            return result;
        } catch (ServiceException e) {
            logger.error("send email for binding Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    /*
     * 修改密保邮箱——2.根据验证链接修改绑定邮箱
     * TODO:
     */
    @Override
    public Result modifyEmailByPassportId(String userId, int clientId, String scode)
            throws Exception {
        Result result = new APIResultSupport(false);
        try {
            if (!operateTimesService.checkBindLimit(userId, clientId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDNUM_LIMITED);
                return result;
            }
            boolean saveEmail = true;
            AccountModuleEnum module = AccountModuleEnum.SECURE;
            String newEmail = emailSenderService.checkScodeForEmail(userId, clientId, module, scode, saveEmail);
            if (StringUtil.isEmpty(newEmail)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDEMAIL_FAILED);
                return result;
            }
            if (accountInfoService.modifyBindEmailByPassportId(userId, newEmail) == null) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDEMAIL_FAILED);
                return result;
            }
            emailSenderService.deleteScodeCacheForEmail(userId, clientId, module);
            operateTimesService.incLimitBind(userId, clientId);
            result.setSuccess(true);
            result.setMessage("修改绑定邮箱成功！");
            return result;
        } catch (ServiceException e) {
            logger.error("modify binding email Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    /*
     * 修改密保手机——1.检查原绑定手机短信码，成功则返回secureCode记录成功标志
     */
    @Override
    public Result checkMobileCodeOldForBinding(String userId, int clientId, String smsCode)
            throws Exception {
        Result result = new APIResultSupport(false);
        try {
            if (!operateTimesService.checkBindLimit(userId, clientId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDNUM_LIMITED);
                return result;
            }
            result = checkMobileCodeByPassportId(userId, clientId, smsCode);
            if (result.isSuccess()) {
                result.setDefaultModel("scode", accountSecureService.getSecureCodeModSecInfo(
                        userId, clientId));
            }
            return result;
        } catch (ServiceException e) {
            logger.error("check mobile code old for binding Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    @Override
    public Result bindMobileByPassportId(String passportId, int clientId, String newMobile,
                                         String smsCode, String password, String modifyIp) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            Account account;
            Result smsCodeAndSecureResult = checkBindMobileSmsCodeAndSecure(passportId, clientId, newMobile, smsCode, modifyIp);
            if (!smsCodeAndSecureResult.isSuccess()) {
                return smsCodeAndSecureResult;
            }
            if (!operateTimesService.checkLimitCheckPwdFail(passportId, clientId, AccountModuleEnum.SECURE)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKPWDFAIL_LIMIT);
                return result;
            }
            //TODO 暂时先采用双读，因为外域、手机账号需要双读，后续改成只读，减少一次account查询
            AuthUserApiParams authParams = new AuthUserApiParams(clientId, passportId, Coder.encryptMD5(password));
            result = loginApiManager.webAuthUser(authParams);
//            result = accountService.verifyUserPwdVaild(passportId, password, true);
            if (!result.isSuccess()) {
                operateTimesService.incLimitCheckPwdFail(passportId, clientId, AccountModuleEnum.SECURE);
                return result;
            }
//            account = (Account) result.getDefaultModel();
//            if(account == null || !Strings.isNullOrEmpty(account.getMobile())){
//                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDMOBILE_FAILED);
//                return result;
//            }
            result = bindApiManager.bindMobile(passportId, newMobile);
            //TODO 所有账号只写SG库时此判断即可去掉
            if (!ManagerHelper.readSohuSwitcher() && result.isSuccess()) {
                accountSecureService.updateSuccessFlag(passportId);
            }
            if (!result.isSuccess()) {
                return result;
            }
            operateTimesService.incLimitBind(passportId, clientId);
            operateTimesService.incIPBindTimes(modifyIp);            result.setMessage("绑定手机成功！");
            return result;
        } catch (ServiceException e) {
            logger.error("bind mobile fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    private Result checkBindMobileSmsCodeAndSecure(String passportId, int clientId, String newMobile, String smsCode, String modifyIp) {
        Result result = new APIResultSupport(false);
        try {
            if (operateTimesService.checkIPBindLimit(modifyIp)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result;
            }
            if (!operateTimesService.checkBindLimit(passportId, clientId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDNUM_LIMITED);
                return result;
            }
            result = checkMobileCodeByNewMobile(newMobile, clientId, smsCode);
        } catch (ServiceException e) {
            logger.error("Check BindMobileSmsCode Or Secure Fail, passportId:" + passportId, e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
        return result;
    }

    /*
      * 修改密保手机——2.验证密码或secureCode、新绑定手机短信码，绑定新手机号
      */
    // TODO:等proxyManager修改好之后修改
    @Override
    public Result modifyMobileByPassportId(String passportId, int clientId, String newMobile,
                                           String smsCode, String scode, String modifyIp) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            Result smsCodeAndSecureResult = checkBindMobileSmsCodeAndSecure(passportId, clientId, newMobile, smsCode, modifyIp);
            if (!smsCodeAndSecureResult.isSuccess()) {
                return smsCodeAndSecureResult;
            }
            result = bindApiManager.modifyBindMobile(passportId, newMobile);
            //TODO 所有账号只写SG库时此判断即可去掉
            if (!ManagerHelper.readSohuSwitcher() && result.isSuccess()) {
                accountSecureService.updateSuccessFlag(passportId);
            }
            if (!result.isSuccess()) {
                return result;
            }
            operateTimesService.incLimitBind(passportId, clientId);
            operateTimesService.incIPBindTimes(modifyIp);
            result.setMessage("修改绑定手机成功！");
            return result;
        } catch (ServiceException e) {
            logger.error("modify mobile fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    /*
     * 接口代理OK
     */
    @Override
    public Result modifyQuesByPassportId(String userId, int clientId, String password,
                                         String newQues, String newAnswer, String modifyIp) throws Exception {
        Result result = new APIResultSupport(false);
        try {
            if (operateTimesService.checkIPBindLimit(modifyIp)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
                return result;
            }

            if (!operateTimesService.checkBindLimit(userId, clientId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNTSECURE_BINDNUM_LIMITED);
                return result;
            }
            if (!operateTimesService.checkLimitCheckPwdFail(userId, clientId, AccountModuleEnum.SECURE)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKPWDFAIL_LIMIT);
                return result;
            }
            // 检验账号密码，判断是否正常用户
            UpdateQuesApiParams updateQuesApiParams = new UpdateQuesApiParams();
            updateQuesApiParams.setUserid(userId);
            updateQuesApiParams.setPassword(password);
            updateQuesApiParams.setNewquestion(newQues);
            updateQuesApiParams.setNewanswer(newAnswer);
            updateQuesApiParams.setModifyip(modifyIp);
            result = secureApiManager.updateQues(updateQuesApiParams);
//            if (ManagerHelper.isInvokeProxyApi(userId)) {
//                // 代理接口
//                result = proxySecureApiManager.updateQues(updateQuesApiParams);
//            } else {
//                // SOGOU接口
//                result = sgSecureApiManager.updateQues(updateQuesApiParams);
//            }
            if (!result.isSuccess()) {
                return result;
            }
            operateTimesService.incLimitBind(userId, clientId);
            operateTimesService.incIPBindTimes(modifyIp);
            result.setMessage("绑定密保问题成功！");
            return result;
        } catch (ServiceException e) {
            logger.error("bind secure question fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    /* ------------------------------------修改密保End------------------------------------ */

    /*
     * 验证手机短信随机码——用于新手机验证
     */
    @Override
    public Result checkMobileCodeByNewMobile(String mobile, int clientId, String smsCode) {
        Result result = new APIResultSupport(false);
        try {
            String passportId = commonManager.getPassportIdByUsername(mobile);
            //检查手机账号能否被绑定
            if (!Strings.isNullOrEmpty(passportId)) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONE_BINDED);
                return result;
            }
            return mobileCodeSenderService.checkSmsCode(mobile, clientId, AccountModuleEnum.SECURE, smsCode);
        } catch (Exception e) {
            logger.error("check new mobile code Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    /*
     * 验证手机短信随机码——用于原绑定手机验证
     */
    @Override
    public Result checkMobileCodeByPassportId(String passportId, int clientId, String smsCode)
            throws Exception {
        Result result = new APIResultSupport(false);
        try {
            Account account = accountService.queryNormalAccount(passportId);
            if (account == null) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_NOTHASACCOUNT);
                return result;
            }
            String mobile = account.getMobile();
            return mobileCodeSenderService.checkSmsCode(mobile, clientId, AccountModuleEnum.SECURE, smsCode);
        } catch (ServiceException e) {
            logger.error("check existed mobile code Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
            return result;
        }
    }

    @Override
    public Result logActionRecord(String userId, int clientId, AccountModuleEnum module, String ip,
                                  String note) {
        Result result = new APIResultSupport(false);
        ActionRecord actionRecord = new ActionRecord();
        actionRecord.setUserId(userId);
        actionRecord.setClientId(clientId);
        actionRecord.setAction(module);
        actionRecord.setDate(System.currentTimeMillis());
        actionRecord.setNote(note);
        if ("127.0.0.1".equals(ip)) {
            ip = "";
        }
        actionRecord.setIp(ip);

        accountSecureService.setActionRecord(actionRecord);
        result.setSuccess(true);
        result.setMessage("记录" + module.getDescription() + "成功！");
        return result;
    }

    @Override
    public Result queryLastActionRecord(String userId, int clientId, AccountModuleEnum module) {
        Result result = new APIResultSupport(false);
        ActionStoreRecordDO actionDO = accountSecureService.getLastActionStoreRecord(userId, clientId, module);
        ActionRecordVO record = new ActionRecordVO(actionDO);
        int clientIdRes = actionDO.getClientId();
        record.setType(appConfigService.queryClientName(clientIdRes));
        result.setDefaultModel("record", record);
        return result;
    }

    @Override
    public Result queryActionRecords(String userId, int clientId, AccountModuleEnum module) {
        Result result = new APIResultSupport(false);
        List<ActionRecordVO> recordsVO = Lists.newLinkedList();
        List<ActionStoreRecordDO>
                storeRecords = accountSecureService.getActionStoreRecords(userId, clientId, module);
        if (!CollectionUtils.isEmpty(storeRecords)) {
            for (ActionStoreRecordDO actionDO : storeRecords) {
                ActionRecordVO actionVO = new ActionRecordVO(actionDO);
                if (actionDO != null) {
                    int clientIdRes = actionDO.getClientId();
                    actionVO.setType(appConfigService.queryClientName(clientIdRes));
                }
                recordsVO.add(actionVO);
            }
        }

        result.setDefaultModel("records", recordsVO);
        result.setSuccess(true);
        result.setMessage("获取" + module.getDescription() + "记录成功！");
        return result;
    }

    @Override
    public Result queryAllActionRecords(String userId, int clientId) {
        // TODO:修改返回的List<T>中的T，增加归属地
        Result result = new APIResultSupport(false);
        List<ActionRecordVO> allRecords = Lists.newLinkedList();
        for (AccountModuleEnum module : AccountModuleEnum.values()) {
            List<ActionStoreRecordDO>
                    storeRecords = accountSecureService.getActionStoreRecords(userId, clientId, module);
            if (!CollectionUtils.isEmpty(storeRecords)) {
                for (ActionStoreRecordDO actionDO : storeRecords) {
                    ActionRecordVO actionVO = new ActionRecordVO(actionDO);
                    if (actionDO != null) {
                        int clientIdRes = actionDO.getClientId();
                        actionVO.setType(appConfigService.queryClientName(clientIdRes));
                    }
                    allRecords.add(actionVO);
                }
            }
        }
        result.setDefaultModel("records", allRecords);
        result.setSuccess(true);
        result.setMessage("获取所有记录成功！");
        return result;
    }

    /*
     * 查询安全信息时需要
     */
    private ActionRecordVO queryLastActionRecordPrivate(String userId, int clientId, AccountModuleEnum module) {
        ActionStoreRecordDO actionDO = accountSecureService.getLastActionStoreRecord(userId, clientId, module);
        ActionRecordVO record = new ActionRecordVO(actionDO);
        if (actionDO != null) {
            int clientIdRes = actionDO.getClientId();
            record.setType(appConfigService.queryClientName(clientIdRes));
        }
        return record;
    }

    /**
     * 获取默认昵称
     *
     * @param passportId
     * @return
     */
    private String defaultUniqname(String passportId) {
        if (AccountDomainEnum.THIRD == AccountDomainEnum.getAccountDomain(passportId)) {
            return "搜狗用户";
        }
        return passportId.substring(0, passportId.indexOf("@"));
    }

}