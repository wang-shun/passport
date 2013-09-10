package com.sogou.upd.passport.oauth2.openresource.response.accesstoken;

import com.sogou.upd.passport.common.HttpConstant;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.oauth2.common.exception.OAuthProblemException;
import com.sogou.upd.passport.oauth2.openresource.vo.TaobaoOAuthTokenVO;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: shipengzhi
 * Date: 13-5-28
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
public class TaobaoJSONAccessTokenResponse extends OAuthAccessTokenResponse {

    private TaobaoOAuthTokenVO oAuthTokenDO;

    @Override
    public void setBody(String body) throws OAuthProblemException {
        this.body = body;
        try {
            this.parameters = new ObjectMapper().readValue(this.body, Map.class);
        } catch (Exception e) {
            throw OAuthProblemException.error(ErrorUtil.UNSUPPORTED_RESPONSE_TYPE,
                    "Invalid response! Response body is not " + HttpConstant.ContentType.JSON + " encoded");
        }
    }

    private TaobaoOAuthTokenVO getTaobaoOAuthTokenVO() throws Exception {
        return new ObjectMapper().readValue(this.body, TaobaoOAuthTokenVO.class);
    }

    @Override
    public String getOpenid() {
        try {
            TaobaoOAuthTokenVO taobaoOAuthTokenVO = getTaobaoOAuthTokenVO();
            return taobaoOAuthTokenVO.getTaobao_user_id();
        } catch (Exception e) {
            log.error("Connect OAuthToken Response parse error, connect:taobao, body:" + body, e);
            return "";
        }
    }

    @Override
    public String getNickName() {
        try {
            TaobaoOAuthTokenVO taobaoOAuthTokenVO = getTaobaoOAuthTokenVO();
            return taobaoOAuthTokenVO.getTaobao_user_nick();
        } catch (Exception e) {
            log.error("Connect OAuthToken Response parse error, connect:taobao, body:" + body, e);
            return "";
        }
    }


}
