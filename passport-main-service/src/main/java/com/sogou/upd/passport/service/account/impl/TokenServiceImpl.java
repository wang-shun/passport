package com.sogou.upd.passport.service.account.impl;

import com.sogou.upd.passport.common.CacheConstant;
import com.sogou.upd.passport.common.DateAndNumTimesConstant;
import com.sogou.upd.passport.common.utils.RedisUtils;
import com.sogou.upd.passport.exception.ServiceException;
import com.sogou.upd.passport.model.account.WebRoamDO;
import com.sogou.upd.passport.service.account.TokenService;
import com.sogou.upd.passport.service.account.generator.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: chenjiameng
 * Date: 13-8-21
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TokenServiceImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String saveWapToken(String passportId) throws ServiceException {
        try {
            String token = TokenGenerator.generatorMappToken(passportId);
            redisUtils.setWithinSeconds(token, passportId, DateAndNumTimesConstant.TIME_FIVEMINUTES);
            return token;
        } catch (Exception e) {
            logger.error("saveWapToken Fail, passportId:" + passportId, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String saveWebRoamToken(String passportId) throws ServiceException {
        try {
            String token = TokenGenerator.generatorMappToken(passportId);
            WebRoamDO webRoamDO = new WebRoamDO();
            webRoamDO.setV("1");
            webRoamDO.setPassportId(passportId);
            webRoamDO.setStatus(1);
            webRoamDO.setCt(System.currentTimeMillis());
            redisUtils.setWithinSeconds(buildWebRoamCacheKey(token), webRoamDO.toString(), DateAndNumTimesConstant.TIME_FIVEMINUTES);
            return token;
        } catch (Exception e) {
            logger.error("saveWebRoamToken Fail, passportId:" + passportId, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public String getPassprotIdByWapToken(String token) throws ServiceException {
        try {
            return redisUtils.get(token);
        } catch (Exception e) {
            logger.error("getPassprotIdByWapToken Fail, token:" + token, e);
            throw new ServiceException(e);
        }
    }

    @Override
    public WebRoamDO getWebRoamDOByToken(String token) throws ServiceException {
        try {
            String key = buildWebRoamCacheKey(token);
            String str = redisUtils.get(key);
            WebRoamDO webRoamDO = WebRoamDO.getWebRoamDO(str);
            if (webRoamDO != null) {
                //安全启见、根据 r_key 清除 缓存中 漫游用户信息、仅供使用一次!
                deleteWebRoamDoByToken(token);
            }
            return webRoamDO;
        } catch (Exception e) {
            logger.error("getWebRoamDOByToken Fail, token:" + token, e);
            throw new ServiceException(e);
        }

    }

    @Override
    public void deleteWebRoamDoByToken(String token) throws ServiceException {
        try {
            redisUtils.delete(buildWebRoamCacheKey(token));
        } catch (Exception e) {
            logger.error("deleteWebRoamDoByToken error. token:" + token, e);
            throw new ServiceException(e);
        }
    }

    /**
     * 生成web_roam 缓存key
     *
     * @param token
     * @return
     * @throws ServiceException
     */
    private String buildWebRoamCacheKey(String token) throws ServiceException {
        return CacheConstant.CACHE_KEY_WEB_ROAM + token;
    }

}
