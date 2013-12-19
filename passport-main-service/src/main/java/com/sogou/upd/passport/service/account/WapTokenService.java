package com.sogou.upd.passport.service.account;

import com.sogou.upd.passport.exception.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: chenjiameng
 * Date: 13-8-21
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public interface WapTokenService {
    /**
     * 存储waptoken，有效期为5分钟
     *
     * @param passportId
     * @throws ServiceException
     */
    public String saveWapToken(String passportId) throws ServiceException;

    /**
     * 通过token获取passportId
     *
     * @param token
     * @return
     * @throws ServiceException
     */
    public String getPassprotIdByToken(String token) throws ServiceException;
}