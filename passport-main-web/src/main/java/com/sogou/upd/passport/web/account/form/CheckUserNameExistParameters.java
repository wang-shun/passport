package com.sogou.upd.passport.web.account.form;

import com.google.common.base.Strings;

import com.sogou.upd.passport.common.lang.StringUtil;
import com.sogou.upd.passport.common.utils.PhoneUtil;

import com.sogou.upd.passport.manager.app.ConfigureManager;
import com.sogou.upd.passport.model.app.AppConfig;
import com.sogou.upd.passport.service.app.AppConfigService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;

/**
 * User: mayan
 * Date: 13-4-15 Time: 下午5:15
 */
@Component
public class CheckUserNameExistParameters {
    @NotBlank(message = "用户名不允许为空!")
    private String username;

    private String client_id;

    @AssertTrue(message = "用户账号格式错误")
    private boolean checkAccount() {
        if (Strings.isNullOrEmpty(username)) {
            return true;
        }
        if (username.indexOf("@") == -1) {
            if (!PhoneUtil.verifyPhoneNumberFormat(username)) {
                //个性账号格式是否拼配
                String regx = "[a-z]([a-zA-Z0-9_.]{3,15})";
                if (!username.matches(regx)) {
                    return false;
                }
            }
        } else {
            //邮箱格式
            String regex = "(\\w)+(\\.\\w+)*@([\\w_\\-])+((\\.\\w+)+)";
            return username.matches(regex);
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}