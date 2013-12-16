package com.sogou.upd.passport.web.account.form;

import com.sogou.upd.passport.web.BaseWebParams;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-5-23 Time: 下午7:22 To change this template use
 * File | Settings | File Templates.
 */
public class BaseUserParams extends BaseWebParams {

    @NotBlank(message = "账号不允许为空!")
    protected String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
