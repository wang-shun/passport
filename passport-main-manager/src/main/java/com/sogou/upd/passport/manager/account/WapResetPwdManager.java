package com.sogou.upd.passport.manager.account;

import com.sogou.upd.passport.common.result.Result;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 14-7-4
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public interface WapResetPwdManager {

    /**
     * 验证手机号与验证码是否匹配
     *
     * @param mobile
     * @param clientId
     * @param smsCode
     * @param needScode
     * @return
     * @throws Exception
     */
    public Result checkMobileCodeResetPwd(String mobile, int clientId, String smsCode, boolean needScode) throws Exception;

    /**
     * 给密保手机发送短信验证码。
     *
     * @param mobile
     * @param client_id
     * @return
     */
    public Result sendMobileCaptcha(String mobile, String client_id, String token, String captcha) throws Exception;

}
