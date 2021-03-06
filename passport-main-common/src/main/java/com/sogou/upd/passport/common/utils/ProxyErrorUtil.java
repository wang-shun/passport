package com.sogou.upd.passport.common.utils;

import com.google.common.collect.Maps;
import com.sogou.upd.passport.common.lang.StringUtil;
import org.apache.commons.collections.keyvalue.DefaultMapEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 用于将SHPP返回的error code转换为我们自己的error code
 * User: ligang201716@sogou-inc.com
 * Date: 13-6-7
 * Time: 下午5:55
 */
public class ProxyErrorUtil extends ErrorUtil {

    private static Logger log = LoggerFactory.getLogger(ProxyErrorUtil.class);

    private static final Map<String, String> SHPPERRCODE_SGPPERRCODE_MAP = Maps.newHashMapWithExpectedSize(200);

    static {
        //所有接口共用的错误码
        SHPPERRCODE_SGPPERRCODE_MAP.put("0", SUCCESS);
        SHPPERRCODE_SGPPERRCODE_MAP.put("1", ERR_CODE_COM_REQURIE);
        SHPPERRCODE_SGPPERRCODE_MAP.put("2", INTERNAL_REQUEST_INVALID);

        //authuser 登录接口
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.3", ERR_CODE_ACCOUNT_USERNAME_PWD_ERROR);//用户名或密码不正确
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.4", ERR_CODE_ACCOUNT_NO_ACTIVED_FAILED);//外域用户未激活
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.5", ERR_CODE_ACCOUNT_PHONE_NOBIND);//手机号码没有绑定（wap专用）
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.6", ERR_CODE_ACCOUNT_USERNAME_PWD_ERROR);//校验失败
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.7", ERR_CODE_ACCOUNT_NO_ACTIVED_FAILED);//手机注册的sohu域账号未激活
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.8", ERR_CODE_ACCOUNT_KILLED);// 账号已被锁定
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.9", ERR_CODE_ACCOUNT_USERNAME_PWD_ERROR);//登陆保护用户的stoken错误
        SHPPERRCODE_SGPPERRCODE_MAP.put("authuser.10", ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);//登陆保护用户的stoken错误

        //mobilecaptcha 手机号验证码注册接口
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.3", SYSTEM_UNKNOWN_EXCEPTION); //系统错误
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.4", ERR_CODE_ACCOUNT_REGED);//帐号已经注册，请直接登录
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.5", ERR_CODE_ACCOUNT_PHONE_BINDED);   //手机号已绑定其他账号
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.6", ERR_CODE_ACCOUNT_CANTSENTSMS);  //今天的短信验证码已经达到上限啦
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.7", ERR_CODE_ACCOUNT_CHECKSMSCODE_LIMIT); //今日验证码校验错误次数已超过上限
        SHPPERRCODE_SGPPERRCODE_MAP.put("mobilecaptcha.8", ERR_CODE_ACCOUNT_SMSCODE); //验证码错误或已过期

        // bindmobile 绑定手机号
        SHPPERRCODE_SGPPERRCODE_MAP.put("bindmobile.1", ERR_CODE_ACCOUNT_SMSCODE);//验证码错误
        SHPPERRCODE_SGPPERRCODE_MAP.put("bindmobile.7", ERR_CODE_PHONE_BIND_FREQUENCY_LIMIT);//手机号操作太过频繁

        // unbindmobile 解绑手机号
        SHPPERRCODE_SGPPERRCODE_MAP.put("unbindmobile.1", ERR_CODE_ACCOUNT_SMSCODE);//验证码错误

        //sendcaptcha 发送手机验证码
        SHPPERRCODE_SGPPERRCODE_MAP.put("sendcaptcha.3", ERR_CODE_ACCOUNT_CANTSENTSMS);//发送短信验证码次数超限
        SHPPERRCODE_SGPPERRCODE_MAP.put("sendcaptcha.5", ERR_CODE_ACCOUNT_PHONE_BINDED);//手机号已经绑定了其他账号
        SHPPERRCODE_SGPPERRCODE_MAP.put("sendcaptcha.6", ERR_CODE_ACCOUNT_PHONE_NOBIND);//手机号没有绑定账号
        SHPPERRCODE_SGPPERRCODE_MAP.put("sendcaptcha.7", ERR_CODE_PHONE_UNBIND_FAILED);//手机号没有绑定账号
        SHPPERRCODE_SGPPERRCODE_MAP.put("sendcaptcha.9", ERR_CODE_PHONE_UNBIND_FAILED);//解除绑定手机失败

        //wapgetuserid 查询手机号绑定的账号
        SHPPERRCODE_SGPPERRCODE_MAP.put("wapgetuserid.3", ERR_CODE_ACCOUNT_PHONE_NOBIND);//手机号码没有绑定用户
        SHPPERRCODE_SGPPERRCODE_MAP.put("wapgetuserid.6", SYSTEM_UNKNOWN_EXCEPTION);//查询失败

        //getuserinfo 获取用户信息
        SHPPERRCODE_SGPPERRCODE_MAP.put("getuserinfo.3", INVALID_ACCOUNT);//用户名不存在（如果是根据昵称查询，没有查询到也是返回3）
        SHPPERRCODE_SGPPERRCODE_MAP.put("getuserinfo.4", ERR_CODE_ACCOUNT_PHONE_NOBIND);//手机号码没有绑定
        SHPPERRCODE_SGPPERRCODE_MAP.put("getuserinfo.6", SYSTEM_UNKNOWN_EXCEPTION);//取得用户信息失败

        //checkuniqname 检查用户昵称
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuniqname.3", ERR_CODE_UNIQNAME_ALREADY_EXISTS);//用户昵称已经被使用
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuniqname.4", ERR_CODE_UNIQNAME_FORBID);//用户昵称包含限制词
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuniqname.6", SYSTEM_UNKNOWN_EXCEPTION);//系统异常

        //checkuser 检查用户名是否可以
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuser.3", ERR_CODE_USERID_ILLEGAL);//非法userid
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuser.4", ERR_CODE_USER_ID_EXIST);//用户名已经存在
        SHPPERRCODE_SGPPERRCODE_MAP.put("checkuser.6", SYSTEM_UNKNOWN_EXCEPTION);//查找失败

        //obtainconnecttokeninfo 调用SOHU接口获取第三方用户的token
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1001", ERR_CODE_COM_REQURIE);     //缺少必须参数
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1002", INTERNAL_REQUEST_INVALID);     //code值无效
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1003", ERR_CODE_CONNECT_INVALID_PARAMETER);     //参数无效
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1102", ERR_CODE_CONNECT_TOKEN_INVALID);     //access_token过期
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1103", ERR_CODE_CONNECT_ACCESSTOKEN_NOT_FOUND); //找不到access_token
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1104", ERR_CODE_CONNECT_OPENAPI_ERROR); //第三方返回openapi调用失败
        SHPPERRCODE_SGPPERRCODE_MAP.put("get.1109", ERR_CODE_CONNECT_NOT_SUPPORTED); //SOHU暂时不支持此第三方的API调用

        //getuserinfo 获取第三方用户信息
        SHPPERRCODE_SGPPERRCODE_MAP.put("info.1102", ERR_CODE_CONNECT_TOKEN_INVALID);     //access_token过期
        SHPPERRCODE_SGPPERRCODE_MAP.put("info.1103", ERR_CODE_CONNECT_ACCESSTOKEN_NOT_FOUND); //找不到access_token
        SHPPERRCODE_SGPPERRCODE_MAP.put("info.1104", ERR_CODE_CONNECT_OPENAPI_ERROR); //第三方返回openapi调用失败

    }

    public static Map.Entry<String, String> shppErrToSgpp(String url, String status) {
        String code = getCode(url, status);

        String message = ERR_CODE_MSG_MAP.get(code);

        return new DefaultMapEntry(code, message);
    }

    private static String getCode(String url, String status) {
        int idx = status.indexOf("|");
        if (idx != -1) {
            status = status.substring(0, idx);
        }
        if (StringUtil.isBlank(status)) {
            return SYSTEM_UNKNOWN_EXCEPTION;
        }
        //通过分解url获取 authuser.3 这样的code
        String[] urls = url.split("/");
        String api = urls[urls.length - 1];
        StringBuilder errorCodeBuilder = new StringBuilder(api);
        errorCodeBuilder.append(".");
        errorCodeBuilder.append(status);
        String errorCode = errorCodeBuilder.toString();
        if (SHPPERRCODE_SGPPERRCODE_MAP.containsKey(errorCode)) {
            return SHPPERRCODE_SGPPERRCODE_MAP.get(errorCode);
        }
        //由于某些接口的1，2不是以下错误类型（如:wapbindmobile.2 是验证码错误），所以将这部分代码放在后面
        switch (status) {
            case "0":
                return SUCCESS;
            case "1":
                return ERR_CODE_COM_REQURIE;
            case "2":
                return INTERNAL_REQUEST_INVALID;
        }
        log.warn("Not found Error code corresponding to the information! ErrorCode：" + errorCode);
        return SYSTEM_UNKNOWN_EXCEPTION;
    }
}
