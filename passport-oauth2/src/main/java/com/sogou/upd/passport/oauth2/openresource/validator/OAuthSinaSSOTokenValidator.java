package com.sogou.upd.passport.oauth2.openresource.validator;

import com.sogou.upd.passport.oauth2.common.validators.AbstractValidator;
import com.sogou.upd.passport.oauth2.openresource.parameters.OpenOAuth;

import javax.servlet.http.HttpServletRequest;

public class OAuthSinaSSOTokenValidator extends AbstractValidator<HttpServletRequest> {

    public OAuthSinaSSOTokenValidator() {
        requiredParams.add(OpenOAuth.OAUTH_CLIENT_ID);
        requiredParams.add(OpenOAuth.OAUTH_CLIENT_SECRET);
        requiredParams.add(OpenOAuth.DEFAULT_OPEN_UID);
        requiredParams.add(OpenOAuth.OAUTH_ACCESS_TOKEN);
        requiredParams.add(OpenOAuth.OAUTH_EXPIRES_IN);
        requiredParams.add(OpenOAuth.OAUTH_INSTANCE_ID);

    }

}
