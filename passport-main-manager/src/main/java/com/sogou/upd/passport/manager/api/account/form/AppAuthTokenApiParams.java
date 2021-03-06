package com.sogou.upd.passport.manager.api.account.form;

import com.sogou.upd.passport.common.WapConstant;
import com.sogou.upd.passport.manager.api.BaseApiParams;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * 客户端验证token参数类
 * User: shipengzhi
 * Date: 13-6-7
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
public class AppAuthTokenApiParams extends BaseApiParams {

    @Min(0)
    private int type;  //2：第三方登录（即将废除，请使用type=5）；5：手机应用使用的session token TODO 目前只是用了2，以后可以删除
    @NotBlank(message = "token不允许为空")
    private String token;  //用户登录成功之后通过302跳转传递给服务器端的token

    private int usethirdinfo = WapConstant.USE_PASSPORT_INFO; //如果是第三方用户，选择是否使用地三方账号信息：1--使用第三方用户信息；0--使用sogou passport用户信息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUsethirdinfo() {
        return usethirdinfo;
    }

    public void setUsethirdinfo(int usethirdinfo) {
        this.usethirdinfo = usethirdinfo;
    }
}
