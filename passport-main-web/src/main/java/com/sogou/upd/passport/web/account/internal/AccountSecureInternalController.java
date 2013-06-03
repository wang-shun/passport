package com.sogou.upd.passport.web.account.internal;

import com.google.common.base.Strings;

import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.manager.account.AccountManager;
import com.sogou.upd.passport.manager.account.AccountSecureManager;
import com.sogou.upd.passport.manager.form.AccountSecureInfoParams;
import com.sogou.upd.passport.manager.form.BaseUserParams;
import com.sogou.upd.passport.web.ControllerHelper;
import com.sogou.upd.passport.web.account.action.AccountSecureAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-5-31 Time: 上午10:36 To change this template
 * use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/internal")
public class AccountSecureInternalController {
    private static final Logger logger = LoggerFactory.getLogger(AccountSecureInternalController.class);

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private AccountSecureManager accountSecureManager;

    @RequestMapping(value = "/secure/query", method = RequestMethod.POST)
    @ResponseBody
    public Object querySecureInfo(BaseUserParams params) throws Exception {
        String validateResult = ControllerHelper.validateParams(params);
        if (!Strings.isNullOrEmpty(validateResult)) {
            return Result.buildError(ErrorUtil.ERR_CODE_COM_REQURIE, validateResult);
        }
        String username = params.getUsername();
        int clientId = Integer.parseInt(params.getClient_id());
        String passportId = accountManager.getPassportIdByUsername(username);
        if (passportId == null) {
            return Result.buildError(ErrorUtil.ERR_CODE_ACCOUNT_NOTHASACCOUNT);
        }

        return accountSecureManager.queryAccountSecureInfo(passportId, clientId, false);
    }
}
