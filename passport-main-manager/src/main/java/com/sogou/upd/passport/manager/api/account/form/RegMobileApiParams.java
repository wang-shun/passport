package com.sogou.upd.passport.manager.api.account.form;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-7-4 Time: 上午11:19 To change this template use File | Settings | File Templates.
 */
public class RegMobileApiParams extends BaseMoblieApiParams {

    @NotBlank(message = "密码不允许为空")
    private String password;  //必须为md5

    public RegMobileApiParams(){

    }
    public RegMobileApiParams(String mobile,String password,int client_id){
        this.mobile = mobile;
        this.password = password;
        this.client_id = client_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
