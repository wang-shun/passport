package com.sogou.upd.passport.manager.api.account.impl;

import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.manager.api.BaseProxyManager;
import com.sogou.upd.passport.manager.api.account.RegisterApiManager;
import com.sogou.upd.passport.manager.api.account.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 14-6-16
 * Time: 下午10:09
 * To change this template use File | Settings | File Templates.
 */
@Component("registerApiManager")
public class RegisterApiManagerImpl extends BaseProxyManager implements RegisterApiManager {

    @Autowired
    private RegisterApiManager proxyRegisterApiManager;
    @Autowired
    private RegisterApiManager sgRegisterApiManager;

    @Override
    public Result regMailUser(RegEmailApiParams regEmailApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result regMobileCaptchaUser(RegMobileCaptchaApiParams regMobileCaptchaApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result sendMobileRegCaptcha(BaseMoblieApiParams baseMoblieApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result checkUser(CheckUserApiParams checkUserApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Result regMobileUser(RegMobileApiParams regMobileApiParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
