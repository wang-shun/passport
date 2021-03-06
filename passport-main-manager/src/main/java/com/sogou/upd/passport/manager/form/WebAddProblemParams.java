package com.sogou.upd.passport.manager.form;

import com.sogou.upd.passport.common.validation.constraints.SafeInput;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * User: chenjiameng Date: 13-6-11 Time: 下午5:15 To change this template use File | Settings | File
 * Templates.
 */
public class WebAddProblemParams {
//    @NotBlank(message = "client_id不允许为空!")
//    @Min(0)
    private String clientId= "1120";
//    @NotBlank(message = "passportId不允许为空!")
    private String passportId;
    @NotBlank(message = "请选择反馈类型!")
    private String typeId;

    @SafeInput(message = "输入标题包含非法字符")
    @Length(min = 1, max = 100, message = "反馈标题长度错误，请重新输入")
    @NotBlank(message = "反馈标题不允许为空!")
    private String title;

    @SafeInput(message = "输入内容包含非法字符")
    @Length(min = 1, max = 300, message = "反馈内容长度错误，请重新输入")
    @NotBlank(message = "反馈内容不允许为空!")
    private String content;
    @Length(min = 1, max = 300, message = "邮箱长度错误，请重新输入")
    @NotBlank(message = "email不允许为空!")
    private String email;

    @NotBlank(message = "验证码不允许为空!")
    private String captcha;//验证码
    @NotBlank(message = "标识码不允许为空!")
    private String token;//标识码

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
