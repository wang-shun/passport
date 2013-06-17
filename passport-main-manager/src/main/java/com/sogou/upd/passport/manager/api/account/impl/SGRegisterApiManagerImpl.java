package com.sogou.upd.passport.manager.api.account.impl;

import com.google.common.base.Strings;

import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.parameter.AccountTypeEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.common.utils.PhoneUtil;
import com.sogou.upd.passport.exception.ServiceException;
import com.sogou.upd.passport.manager.account.SecureManager;
import com.sogou.upd.passport.manager.api.account.RegisterApiManager;
import com.sogou.upd.passport.manager.api.account.form.BaseMoblieApiParams;
import com.sogou.upd.passport.manager.api.account.form.CheckUserApiParams;
import com.sogou.upd.passport.manager.api.account.form.RegEmailApiParams;
import com.sogou.upd.passport.manager.api.account.form.RegMobileCaptchaApiParams;
import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.service.account.AccountService;
import com.sogou.upd.passport.service.account.MobileCodeSenderService;
import com.sogou.upd.passport.service.account.MobilePassportMappingService;
import com.sogou.upd.passport.service.account.generator.PassportIDGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册
 * User: mayan
 * Date: 13-6-8
 * Time: 下午9:50
 */
@Component("sgRegisterApiManager")
public class SGRegisterApiManagerImpl implements RegisterApiManager {

    private static Logger logger = LoggerFactory.getLogger(SGRegisterApiManagerImpl.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private SecureManager secureManager;
    @Autowired
    private MobileCodeSenderService mobileCodeSenderService;
    @Autowired
    private MobilePassportMappingService mobilePassportMappingService;

    @Override
    public Result regMailUser(RegEmailApiParams params) {
      Result result = new APIResultSupport(false);
      try {
        String username=params.getUserid();
        String password=params.getPassword();
        String ip=params.getCreateip();
        int clientId=params.getClient_id();

        //判断注册账号类型，外域用户还是个性用户
        AccountDomainEnum emailType = AccountDomainEnum.getAccountDomain(username);
        switch (emailType) {
          case SOGOU://个性账号直接注册
          case UNKNOWN:
            Account account = accountService.initialAccount(username,password , false, ip, AccountTypeEnum
                .EMAIL.getValue());
            if (account != null) {
              result.setSuccess(true);
              result.setMessage("注册成功！");
            } else {
              result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGISTER_FAILED);
            }
            break;
          case OTHER://外域邮件注册
            boolean isSendSuccess = accountService.sendActiveEmail(username, password, clientId, ip);
            if (isSendSuccess) {
              result.setSuccess(true);
              result.setMessage("感谢注册，请立即激活账户！");
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


    @Override
    public Result regMobileCaptchaUser(RegMobileCaptchaApiParams regParams) {

        Result result = new APIResultSupport(false);
        try {

          int clientId =regParams.getClient_id();
          String mobile = regParams.getMobile();
          String password = regParams.getPassword();
          String ip=regParams.getIp();

          String captcha = regParams.getCaptcha();
          //验证手机号码与验证码是否匹配
          boolean checkSmsInfo = mobileCodeSenderService.checkSmsInfoFromCache(mobile, captcha, clientId);
          if (!checkSmsInfo) {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_PHONE_NOT_MATCH_SMSCODE);
            return result;
          }

          Account account = accountService.initialAccount(mobile,password , false, ip, AccountTypeEnum
              .PHONE.getValue());
          if (account != null) {
            result.setSuccess(true);
            result.setMessage("注册成功！");
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
      if (PhoneUtil.verifyPhoneNumberFormat(username)) {
        String passportId = mobilePassportMappingService.queryPassportIdByMobile(username);
        if (!Strings.isNullOrEmpty(passportId)) {
          result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
        }
        return result;
      } else {
        Account account = accountService.queryAccountByPassportId(username);
        if (account != null) {
          result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_REGED);
        }
        return result;
      }
    } catch (ServiceException e) {
      logger.error("Check account is exists Exception, username:" + username, e);
    }
    result.setSuccess(true);
    result.setMessage("账户未被占用，可以注册");
    return result;
  }

    @Override
    public Result sendMobileRegCaptcha(BaseMoblieApiParams params) {
      Result result = new APIResultSupport(false);
      try {
        result=secureManager.sendMobileCode(params.getMobile(),params.getClient_id());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return result;
    }
}
