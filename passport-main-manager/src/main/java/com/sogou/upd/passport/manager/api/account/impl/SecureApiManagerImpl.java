package com.sogou.upd.passport.manager.api.account.impl;

import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.manager.ManagerHelper;
import com.sogou.upd.passport.manager.api.account.SecureApiManager;
import com.sogou.upd.passport.manager.api.account.form.GetSecureInfoApiParams;
import com.sogou.upd.passport.manager.api.account.form.ResetPasswordBySecQuesApiParams;
import com.sogou.upd.passport.manager.api.account.form.UpdatePwdApiParams;
import com.sogou.upd.passport.manager.api.account.form.UpdateQuesApiParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 14-6-26
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
@Component("secureApiManager")
public class SecureApiManagerImpl implements SecureApiManager {

    @Autowired
    private SecureApiManager sgSecureApiManager;
    @Autowired
    private SecureApiManager proxySecureApiManager;

    @Override
    public Result updatePwd(UpdatePwdApiParams updatePwdApiParams) {
        Result result = new APIResultSupport(false);
        if (ManagerHelper.writeSohuSwitcher()) {
            result = proxySecureApiManager.updatePwd(updatePwdApiParams);
        } else {
            if (AccountDomainEnum.SOGOU.equals(AccountDomainEnum.getAccountDomain(updatePwdApiParams.getUserid()))) {
//                result = bothUpdatePwd(updatePwdApiParams);
            } else {
                result = proxySecureApiManager.updatePwd(updatePwdApiParams);
            }
        }
        return result;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result updateQues(UpdateQuesApiParams updateQuesApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result getUserSecureInfo(GetSecureInfoApiParams getSecureInfoApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result resetPasswordByQues(ResetPasswordBySecQuesApiParams resetPasswordBySecQuesApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resetPwd(UpdatePwdApiParams updatePwdApiParams) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
