package com.sogou.upd.passport.dao.account;

import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.model.account.AccountAuth;

import java.util.Map;

/**
 * User: mayan
 * Date: 13-3-22
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */

/**
 * 用户主表的接口mapper文件
 */
public interface AccountMapper {
    /**
     * 根据传入的参数，手机号码和密码,查询该手机是否已经注册
     * @return
     */
    public Account checkIsRegisterAccount(Account account);
    /**
     * 验证合法，用户注册
     * @param account
     */
    public int userRegister(Account account);
    /**
     * 根据用户名密码获取用户Account
     * @param
     * @return
     */
    public Account getUserAccount(Map<String,String> queryMap);

    /**
     * 根据passportId查询Account
     * @param passportId
     * @return
     */
    public Account getAccountByPassportId(String passportId);

    /**
     * 根据主键id获取passportId
     * @param userId
     * @return
     */
    public String getPassportIdByUserId(long userId);

    /**
     * 根据passportId查询对应的主键Id
     * @param passportId
     * @return
     */
    public long getUserIdByPassportId(String passportId);

}
