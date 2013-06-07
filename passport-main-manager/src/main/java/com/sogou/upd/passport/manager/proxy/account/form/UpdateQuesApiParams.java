package com.sogou.upd.passport.manager.proxy.account.form;

import com.sogou.upd.passport.manager.proxy.BaseApiParameters;
import org.hibernate.validator.constraints.NotBlank;

/**
 * User: ligang201716@sogou-inc.com
 * Date: 13-6-7
 * Time: 下午1:49
 */
public class UpdateQuesApiParams  extends BaseApiParameters {

    @NotBlank(message = "账号不能为空！")
    private String passport_id;

    @NotBlank(message = "原密码不能为空！")
    private String password;

    @NotBlank(message = "密保问题不能为空！")
    private String newquestion;

    @NotBlank(message = "密保答案不能为空！")
    private String newanswer;

    @NotBlank(message = "修改密保的Ip不能为空！")
    private String modifyip;

    public String getModifyip() {
        return modifyip;
    }

    public void setModifyip(String modifyip) {
        this.modifyip = modifyip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(String passport_id) {
        this.passport_id = passport_id;
    }

    public String getNewquestion() {
        return newquestion;
    }

    public void setNewquestion(String newquestion) {
        this.newquestion = newquestion;
    }

    public String getNewanswer() {
        return newanswer;
    }

    public void setNewanswer(String newanswer) {
        this.newanswer = newanswer;
    }
}
