package com.sogou.upd.passport.common;

/**
 * Wap登陆使用的常量类 User: ligang201716@sogou-inc.com Date: 13-5-13 Time: 下午4:55
 */
public class WapConstant {

    /**
     * JSON格式标识
     */
    public static final String WAP_JSON = "0";
    /**
     * 简版页面标识
     */
    public static final String WAP_SIMPLE = "1";

    /**
     * 彩屏版页面标识
     */
    public static final String WAP_COLOR = "2";

    /**
     * 触摸版页面标识
     */
    public static final String WAP_TOUCH = "5";

    // wap版颜色为绿色
    public static final String WAP_SKIN_GREEN = "green";
    // wap版颜色为红色
    public static final String WAP_SKIN_RED = "red";
    //wap版颜色与浏览器统一
    public static final String WAP_SKIN_SE = "se";
    //输入法的手游H5页面使用
    public static final String WAP_SKIN_ORANGE = "orange";
    //搜索本地生活定制shenghuo为橙色，色值#fe6847
    public static final String WAP_SKIN_SHENGHUO = "shenghuo";
    //搜狗阅读短验登录定制
    public static final String WAP_SKIN_READ = "read";


    //默认登录来源
    public static final String WAP_INDEX = "http://wap.sogou.com/";

    //需要使用第三方用户信息
    public static final int USE_THIRD_INFO = 1;

    //使用passport用户信息
    public static final int USE_PASSPORT_INFO = 0;


}
