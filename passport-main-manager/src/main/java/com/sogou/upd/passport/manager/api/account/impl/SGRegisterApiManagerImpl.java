package com.sogou.upd.passport.manager.api.account.impl;

import com.google.common.base.Strings;
import com.sogou.upd.passport.common.CommonHelper;
import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.parameter.AccountModuleEnum;
import com.sogou.upd.passport.common.parameter.AccountTypeEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.common.utils.PhoneUtil;
import com.sogou.upd.passport.common.validation.constraints.UserNameValidator;
import com.sogou.upd.passport.exception.ServiceException;
import com.sogou.upd.passport.manager.account.RegManager;
import com.sogou.upd.passport.manager.account.SecureManager;
import com.sogou.upd.passport.manager.api.BaseProxyManager;
import com.sogou.upd.passport.manager.api.account.RegisterApiManager;
import com.sogou.upd.passport.manager.api.account.form.*;
import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.model.account.AccountInfo;
import com.sogou.upd.passport.service.account.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 注册
 * User: mayan
 * Date: 13-6-8
 * Time: 下午9:50
 */
@Component("sgRegisterApiManager")
public class SGRegisterApiManagerImpl extends BaseProxyManager implements RegisterApiManager {
    private static Logger logger = LoggerFactory.getLogger(SGRegisterApiManagerImpl.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private SecureManager secureManager;
    @Autowired
    private MobileCodeSenderService mobileCodeSenderService;
    @Autowired
    private MobilePassportMappingService mobilePassportMappingService;
    @Autowired
    private SnamePassportMappingService snamePassportMappingService;
    @Autowired
    private RegManager regManager;
    @Autowired
    private UserNameValidator userNameValidator;

    @Override
    public Result regMailUser(RegEmailApiParams params) {
        Result result = new APIResultSupport(false);
        try {
            String username = params.getUserid();
            String password = params.getPassword();
            String ip = params.getCreateip();
            int clientId = params.getClient_id();
            //判断注册账号类型，外域用户还是个性用户
            AccountDomainEnum emailType = AccountDomainEnum.getAccountDomain(username);
            boolean flag = userNameValidator.isValid(username, null);
            if (!flag) {
                result = new APIResultSupport(false);
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                return result;
            }
            //正式注册时需要检测用户是否已经注册过
            CheckUserApiParams checkUserApiParams = buildProxyApiParams(username, clientId);
            result = checkUser(checkUserApiParams);
            if (!result.isSuccess()) {
                result = new APIResultSupport(false);
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
                return result;
            }
            switch (emailType) {
                case SOGOU://个性账号直接注册
                case INDIVID:
                    Account account = accountService.initialAccount(username, password, true, ip, AccountTypeEnum
                            .SOGOU.getValue());
                    if (account != null) {
                        AccountInfo accountInfo = new AccountInfo();
                        accountInfo.setPassportId(account.getPassportId());
                        accountInfo.setCreateTime(new Date());
                        accountInfo.setUpdateTime(new Date());
                        accountInfo.setModifyip(ip);
                        boolean isUpdateSuccess = accountInfoService.updateAccountInfo(accountInfo);
                        if (!isUpdateSuccess) {
                            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
                        } else {
                            result.setSuccess(true);
                            result.setDefaultModel("userid", account.getPassportId());
                            result.setMessage("注册成功");
                            result.setDefaultModel("isSetCookie", true);
                        }
                    } else {
                        result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
                    }
                    break;
                case OTHER://外域邮件注册
                    String ru = params.getRu();
                    boolean isSendSuccess = accountService.sendActiveEmail(username, password, clientId, ip, ru);
                    if (isSendSuccess) {
                        result.setSuccess(true);
                        result.setMessage("感谢注册，请立即激活账户！");
                        result.setDefaultModel("userid", username);
                        result.setDefaultModel("isSetCookie", false);
                    } else {
                        result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
                    }
                    break;
            }
            return result;
        } catch (Exception e) {
            logger.error("mail register account Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
        }
        return result;
    }

    private CheckUserApiParams buildProxyApiParams(String username, int clientId) {
        CheckUserApiParams checkUserApiParams = new CheckUserApiParams();
        checkUserApiParams.setUserid(username);
        checkUserApiParams.setClient_id(clientId);
        return checkUserApiParams;
    }


    @Override
    public Result regMobileCaptchaUser(RegMobileCaptchaApiParams regParams) {

        Result result = new APIResultSupport(false);
        try {

            int clientId = regParams.getClient_id();
            String mobile = regParams.getMobile();
            String password = regParams.getPassword();
            String ip = regParams.getIp();

            String captcha = regParams.getCaptcha();
            //验证手机号码与验证码是否匹配
            result = mobileCodeSenderService.checkSmsCode(mobile, clientId, AccountModuleEnum.REGISTER, captcha);
            if (!result.isSuccess()) {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONE_NOT_MATCH_SMSCODE);
                return result;
            }

            Account account = accountService.initialAccount(mobile, password, false, ip, AccountTypeEnum
                    .PHONE.getValue());
            if (account != null) {
                result.setSuccess(true);
                result.setDefaultModel("userid", account.getPassportId());
                result.setMessage("注册成功");
                result.setDefaultModel("isSetCookie", true);
                result.setDefaultModel(account);
            } else {
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
            }
        } catch (Exception e) {
            logger.error("mobile register phone account Fail:", e);
            result.setCode(ErrorUtil.SYSTEM_UNKNOWN_EXCEPTION);
        }
        return result;
    }

    @Override
    public Result checkUser(CheckUserApiParams checkUserApiParams) {
        Result result = new APIResultSupport(false);
        String username = null;
        try {
            username = checkUserApiParams.getUserid();
            if (username.indexOf("@") == -1) {
                //判断是否是手机号注册
                if (!PhoneUtil.verifyPhoneNumberFormat(username)) {
                    username = username + "@sogou.com";
                }
            }
            //如果是手机账号注册
            if (PhoneUtil.verifyPhoneNumberFormat(username)) {
                String passportId = mobilePassportMappingService.queryPassportIdByMobile(username);
                if (!Strings.isNullOrEmpty(passportId)) {
                    result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
                    result.setDefaultModel("userid", passportId);
                    return result;
                }
            } else {
                //如果是外域或个性账号注册
                Account account = accountService.queryNormalAccount(username.toLowerCase());
                if (account != null) {
                    result.setCode(ErrorUtil.ERR_CODE_USER_ID_EXIST);
                    result.setDefaultModel("flag", String.valueOf(account.getFlag()));
                    result.setDefaultModel("userid", account.getPassportId());
                    return result;
                }
            }
            int clientId = checkUserApiParams.getClient_id();
            if (CommonHelper.isExplorerToken(clientId)) {
                result = isSohuplusUser(username);
            } else {
                result.setSuccess(true);
                result.setMessage("操作成功");
            }
        } catch (ServiceException e) {
            logger.error("Check account is exists Exception, username:" + username, e);
            throw new ServiceException(e);
        }
        return result;
    }

    /*
     * client=1044的username为个性域名或手机号
     * 都有可能是sohuplus的账号，需要判断sohuplus映射表
     * 如果username包含@，则取@前面的
     */
    private Result isSohuplusUser(String username) {
        Result result = new APIResultSupport(false);
        if (username.contains("@")) {
            username = username.substring(0, username.indexOf("@"));
        }
        String sohuplus_passportId = snamePassportMappingService.queryPassportIdBySnameOrPhone(username);
        if (!Strings.isNullOrEmpty(sohuplus_passportId)) {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
            return result;
        } else {
            result.setSuccess(true);
            result.setMessage("操作成功");
        }
        return result;
    }

    @Override
    public Result sendMobileRegCaptcha(BaseMoblieApiParams params) {
        Result result = new APIResultSupport(false);
        String mobile = params.getMobile();
        try {
            BaseMoblieApiParams baseMoblieApiParams = new BaseMoblieApiParams();
            baseMoblieApiParams.setMobile(mobile);
            //检测手机号是否已经注册或绑定
            result = regManager.isAccountNotExists(mobile, params.getClient_id());
            if (!result.isSuccess()) {
                result.setSuccess(false);
                result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONE_BINDED);
                result.setMessage("手机号已绑定其他账号");
                return result;
            }
            result = secureManager.sendMobileCode(params.getMobile(), params.getClient_id(), AccountModuleEnum.REGISTER);
        } catch (Exception e) {
            logger.error("send mobile code Fail, mobile:" + mobile, e);
        }
        return result;
    }

    @Override
    public Result regMobileUser(RegMobileApiParams regMobileApiParams) {
        Result result = new APIResultSupport(false);
        String mobile = regMobileApiParams.getMobile();
        String password = regMobileApiParams.getPassword();
        try {
            if (PhoneUtil.verifyPhoneNumberFormat(mobile)) {
                String passportId = mobilePassportMappingService.queryPassportIdByMobile(mobile);
                if (!Strings.isNullOrEmpty(passportId)) {
                    result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
                    return result;
                }
                Account account = accountService.initialAccount(mobile, password, true, null, AccountTypeEnum
                        .PHONE.getValue());
                if (account != null) {
                    result.setSuccess(true);
                    result.setDefaultModel("userid", account.getPassportId());
                    result.setMessage("注册成功！");
                    result.setDefaultModel("isSetCookie", true);
                } else {
                    result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
                    return result;
                }
            } else {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                return result;
            }
        } catch (Exception e) {
            logger.error("mobile reg without captcha failed,mobile is {} ", mobile, e);
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
        }
        return result;
    }
}
