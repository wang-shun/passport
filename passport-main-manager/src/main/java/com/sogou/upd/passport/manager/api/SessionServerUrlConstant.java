package com.sogou.upd.passport.manager.api;

/**
 * User: mayan
 * Date: 13-12-4
 * Time: 下午9:11
 */
public class SessionServerUrlConstant {
    private static final String BASE_URL = "http://session.account.sogou/";
    public static final String CREATE_SESSION = BASE_URL + "new_session";
    public static final String REMOVE_SESSION = BASE_URL + "del_session";
    public static final String VERIFY_SID = BASE_URL + "verify_sid";
}
