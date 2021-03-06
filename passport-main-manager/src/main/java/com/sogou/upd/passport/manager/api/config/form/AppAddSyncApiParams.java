package com.sogou.upd.passport.manager.api.config.form;

import com.google.common.base.Objects;

import com.sogou.upd.passport.manager.api.BaseApiParams;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * 同步应用参数
 */
public class AppAddSyncApiParams extends BaseApiParams {
    @Min(10000)
    private int appId;
    @NotBlank(message = "应用名称不允许为空")
    private String appName;
    @NotBlank(message = "服务端密钥不允许为空")
    private String serverSecret;
    @NotBlank(message = "客户端密钥不允许为空")
    private String clientSecret;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerSecret() {
        return serverSecret;
    }

    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("appId", appId)
                .add("appName", appName)
                .add("serverSecret", serverSecret)
                .add("clientSecret", clientSecret)
                .toString();
    }
}
