package com.sogou.upd.passport.web.account.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-5-23 Time: 下午2:57 To change this template use
 * File | Settings | File Templates.
 */
public class BaseAccountParams {

    @NotBlank(message = "账号不允许为空!")
    protected String userid;
    @NotBlank(message = "client_id不允许为空!")
    @Min(0)
    protected String client_id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}