package com.sogou.upd.passport.service.account;

import com.sogou.upd.passport.exception.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: shipengzhi
 * Date: 13-4-22
 * Time: 下午9:51
 * To change this template use File | Settings | File Templates.
 */
public interface MobilePassportMappingService {

    /**
     * 根据手机号码获取passportId
     *
     * @param mobile
     * @return 获取不到则抛出异常
     * @throws com.sogou.upd.passport.exception.ServiceException
     */
    public String queryPassportIdByMobile(String mobile) throws ServiceException;

    /**
     * 根据用户名
     * @param username
     * @return
     * @throws ServiceException
     */
    public String queryPassportIdByUsername(String username) throws ServiceException;

    /**
     * 插入一条mobile和passportId的映射关系
     *
     * @param mobile
     * @param passportId
     * @return
     * @throws com.sogou.upd.passport.exception.ServiceException
     */
    public boolean initialMobilePassportMapping(String mobile, String passportId) throws ServiceException;

    /**
     * 删除mobile和passportId的映射关系
     *
     * @param mobile
     * @return
     * @throws com.sogou.upd.passport.exception.ServiceException
     */
    public boolean deleteMobilePassportMapping(String mobile) throws ServiceException;

}
