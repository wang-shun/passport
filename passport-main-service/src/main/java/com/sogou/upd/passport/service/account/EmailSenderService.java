package com.sogou.upd.passport.service.account;

import com.sogou.upd.passport.exception.ServiceException;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-5-7 Time: 下午2:00 To change this template use
 * File | Settings | File Templates.
 */
public interface EmailSenderService {
    /**
     * 发送密码重置申请邮件
     *
     * @param uid
     * @param clientId
     * @param address
     */
    public boolean sendEmailForResetPwd(String uid, int clientId, String address) throws ServiceException;

    /**
     * 验证密码重置申请邮件
     *
     * @param uid
     * @param clientId
     * @param token
     */
    public boolean checkEmailForResetPwd(String uid, int clientId, String token) throws ServiceException;

    /**
     * 删除邮件链接token缓存
     * @param uid
     * @param clientId
     * @return
     * @throws ServiceException
     */
    public boolean deleteEmailCacheResetPwd(String uid, int clientId) throws ServiceException;

    /**
     * 检查邮件发送次数限制
     * @param email
     * @param clientId
     * @return 不超过限制，返回true；超过，返回false
     * @throws ServiceException
     */
    public boolean checkSendEmailForPwdLimited(String email, int clientId) throws ServiceException;

    /**
     * 发送绑定邮箱验证邮件
     *
     * @param uid 目前为passportId
     * @param clientId
     * @param address 待绑定邮箱
     * @return
     * @throws ServiceException
     */
    public boolean sendEmailForBinding(String uid, int clientId, String address) throws ServiceException;

    /**
     * 检查邮件发送次数限制
     *
     * @param email
     * @param clientId
     * @return
     * @throws ServiceException
     */
    public boolean checkSendEmailNumForBinding(String email, int clientId) throws ServiceException;

    /**
     * 验证邮件token，返回待绑定邮箱
     *
     * @param uid
     * @param clientId
     * @param token
     * @return new email for binding
     * @throws ServiceException
     */
    String checkEmailForBinding(String uid, int clientId, String token) throws ServiceException;



    /**
     * 删除验证token缓存
     *
     * @param uid
     * @param clientId
     * @return
     * @throws ServiceException
     */
    boolean deleteEmailCacheForBinding(String uid, int clientId) throws ServiceException;
}
