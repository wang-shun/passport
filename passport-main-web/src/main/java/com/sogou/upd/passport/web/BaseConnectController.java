package com.sogou.upd.passport.web;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.CommonHelper;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.common.utils.ServletUtil;
import com.sogou.upd.passport.oauth2.common.parameters.QueryParameterApplier;
import com.sogou.upd.passport.oauth2.common.types.ConnectTypeEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shipengzhi
 * Date: 13-4-26
 * Time: 下午4:46
 * To change this template use File | Settings | File Templates.
 */
public class BaseConnectController extends BaseController {

    /**
     * 为防止CRSF攻击，OAuth登录授权需传递state参数
     * 种cookie，uuid=provider_state
     */
    protected void writeOAuthStateCookie(HttpServletResponse res, String uuid, String providerStr) {
        String cookieValue = CommonHelper.constructStateCookieKey(providerStr);
        ServletUtil.setCookie(res, uuid, cookieValue, CommonConstant.DEFAULT_COOKIE_EXPIRE);
    }

    /**
     * 第三方登录接口type=mapp、mobile、web时，需要对ru分别做处理
     *
     * @param type      /connect/login接口的type参数
     * @param ru        回调url
     * @return
     */
    protected String buildAppSuccessRu(String type, String ru, String succValue){
        Map params = Maps.newHashMap();
        try {
            ru = URLDecoder.decode(ru, CommonConstant.DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("Url decode Exception! ru:" + ru);
            ru = CommonConstant.DEFAULT_CONNECT_REDIRECT_URL;
        }
        if (type.equals(ConnectTypeEnum.MAPP.toString())) {
            params.put("token", succValue);
            ru = QueryParameterApplier.applyOAuthParametersString(ru, params);
        } else if (type.equals(ConnectTypeEnum.Mobile.toString())) {
            params.put("s_m_u", succValue);
            ru = QueryParameterApplier.applyOAuthParametersString(ru, params);
        }
        return ru;
    }

    /**
     * 第三方登录接口type=mapp时，需要在ru后追加status和statusText
     *
     * @param type      /connect/login接口的type参数
     * @param ru        回调url
     * @param errorCode 错误码
     * @param errorText 错误文案
     * @return
     */
    protected String buildAppErrorRu(String type, String ru, String errorCode, String errorText) {
        if (Strings.isNullOrEmpty(ru)) {
            ru = CommonConstant.DEFAULT_CONNECT_REDIRECT_URL;
        }
        try {
            ru = URLDecoder.decode(ru, CommonConstant.DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("Url decode Exception! ru:" + ru);
            ru = CommonConstant.DEFAULT_CONNECT_REDIRECT_URL;
        }
        if (ConnectTypeEnum.isMobileApp(type) && !Strings.isNullOrEmpty(errorCode)) {
            Map params = Maps.newHashMap();
            params.put(CommonConstant.RESPONSE_STATUS, errorCode);
            if (Strings.isNullOrEmpty(errorText)) {
                errorText = ErrorUtil.ERR_CODE_MSG_MAP.get(errorCode);
            }
            params.put(CommonConstant.RESPONSE_STATUS_TEXT, errorText);
            ru = QueryParameterApplier.applyOAuthParametersString(ru, params);
        }
        return ru;
    }


}
