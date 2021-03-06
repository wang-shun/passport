package com.sogou.upd.passport.manager.api.account.form;

import com.sogou.upd.passport.manager.api.account.BaseRegUserApiParams;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 检查用户名是否存在api
 * User: ligang201716@sogou-inc.com
 * Date: 13-6-9
 * Time: 下午2:04
 */
public class CheckUserApiParams extends BaseRegUserApiParams {


    /**
     * 业务线传过来的用户真实IP
     */
    @NotBlank(message = "createip不允许为空")
    private String createip;

    public String getCreateip() {
        return createip;
    }

    public void setCreateip(String createip) {
        this.createip = createip;
    }

}
