package com.sogou.upd.passport.manager.api.connect;

import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.model.app.AppConfig;

/**
 * User: mayan
 * Date: 13-12-4
 * Time: 下午8:43
 */
public interface SessionServerManager {
     public Result createSession(String userId);
     public Result removeSession(String sgid);
     public Result getPassportIdBySgid(String sgid,String ip);
}
