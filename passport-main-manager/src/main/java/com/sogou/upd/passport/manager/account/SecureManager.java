package com.sogou.upd.passport.manager.account;

import com.sogou.upd.passport.common.parameter.AccountModuleEnum;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.manager.form.MobileModifyPwdParams;
import com.sogou.upd.passport.manager.form.ResetPwdParameters;

/**
 * 账户安全相关 User: mayan Date: 13-4-15 Time: 下午4:30 To change this template use File | Settings | File
 * Templates.
 */
public interface SecureManager {

    /**
     * 发送短信验证码（至未注册未绑定手机）
     */
    public Result sendMobileCode(String mobile, int clientId) throws Exception;

    /**
     * 发送短信验证码（根据passportId）
     */
    public Result sendMobileCodeByPassportId(String passportId, int clientId) throws Exception;

    /**
     * 发送手机验证码，不检测是否已注册或绑定，暂时供sendMobileCode*方法调用
     *
     * @param mobile
     * @param clientId
     * @return
     * @throws Exception
     */
    public Result sendSmsCodeToMobile(String mobile, int clientId) throws Exception;

    /**
     * 重发验证码时更新缓存状态
     */
    public Result updateSmsCacheInfo(String cacheKey, int clientId);

    /**
     * 检查发送邮件限制
     *
     * @param passportId
     * @param clientId
     * @param module
     * @param email
     * @return
     * @throws Exception
     */
    public Result checkLimitSendEmail(String passportId, int clientId, AccountModuleEnum module,
                                      String email) throws Exception;

    /**
     * 手机用户找回密码
     *
     * @param mobile   手机号码
     * @param clientId 客户端ID
     * @return Result格式的返回值，成功则发送验证码；失败，提示失败信息
     */
    public Result findPassword(String mobile, int clientId);

    /**
     * 手机用户重置密码
     *
     * @return Result格式的返回值, 成功或失败，返回提示信息
     */
    public Result resetPassword(MobileModifyPwdParams regParams) throws Exception;

    /**
     * 查询账户安全信息，包括邮箱、手机、密保问题，并模糊处理
     *
     * @param passportId
     * @param clientId
     * @param doProcess 是否模糊处理，如abcde@sogou.com转换为ab*****e@sogou.com
     * @return result.getData().get("data") // 账户安全信息
     * @throws Exception
     */
    public Result queryAccountSecureInfo(String passportId, int clientId, boolean doProcess) throws Exception;

    /**
     * 重置用户密码（web验证码方式）
     */
    public Result resetWebPassword(ResetPwdParameters resetPwdParameters) throws Exception;


    /* ------------------------------------修改密保Begin------------------------------------ */

    /**
     * 修改密保邮箱——1.验证原绑定邮箱及发送邮件至待绑定邮箱
     *
     * @param passportId
     * @param clientId
     * @param newEmail
     * @param oldEmail
     * @return
     * @throws Exception
     */
    public Result sendEmailForBinding(String passportId, int clientId, String password, String newEmail,
                                      String oldEmail) throws Exception;

    /**
     * 修改密保邮箱——2.根据验证链接修改绑定邮箱
     *
     * @param passportId
     * @param clientId
     * @param scode
     * @return
     * @throws Exception
     */
    public Result modifyEmailByPassportId(String passportId, int clientId, String scode) throws Exception;

    /**
     * 修改密保手机——1.检查原绑定手机短信码，成功则返回secureCode记录成功标志
     *
     * @param passportId
     * @param clientId
     * @param smsCode
     * @return
     * @throws Exception
     */
    public Result checkMobileCodeOldForBinding(String passportId, int clientId, String smsCode) throws Exception;

    /**
     * 绑定密保手机——2.首次绑定密保手机，验证密码、新绑定手机短信码，绑定新手机号
     *
     * @param passportId
     * @param clientId
     * @param newMobile
     * @param smsCode
     * @param password
     * @return
     * @throws Exception
     */
    public Result bindMobileByPassportId(String passportId, int clientId, String newMobile,
                                         String smsCode, String password) throws Exception;

    /**
     * 修改密保手机——2.修改密保手机，验证scode、新绑定手机短信码，绑定新手机号
     *
     * @param passportId
     * @param clientId
     * @param newMobile 新绑定手机号
     * @param smsCode   新绑定手机号短信验证码
     * @param scode 验证安全码
     * @return
     * @throws Exception
     */
    public Result modifyMobileByPassportId(String passportId, int clientId, String newMobile,
                                           String smsCode, String scode) throws Exception;

    /**
     * 修改密保问题——验证密码，绑定新问题和答案
     *
     * @param passportId
     * @param clientId
     * @param password
     * @param newQues
     * @param newAnswer
     * @return
     * @throws Exception
     */
    public Result modifyQuesByPassportId(String passportId, int clientId, String password,
                                         String newQues, String newAnswer, String modifyIp) throws Exception;

    /* ------------------------------------修改密保End------------------------------------ */

    /**
     * 验证手机短信随机码——用于新手机验证，不分业务功能
     *
     * @param mobile
     * @param clientId
     * @param smsCode
     * @return
     * @throws Exception
     */
    public Result checkMobileCodeByNewMobile(String mobile, int clientId, String smsCode) throws Exception;

    /**
     * 验证手机短信随机码——用于原绑定手机验证，不分业务功能
     *
     * @param passportId
     * @param clientId
     * @param smsCode
     * @return
     * @throws Exception
     */
    public Result checkMobileCodeByPassportId(String passportId, int clientId, String smsCode) throws Exception;

}