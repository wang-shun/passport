package com.sogou.upd.passport.web.connect;

import com.sogou.upd.passport.common.parameter.AccountTypeEnum;
import com.sogou.upd.passport.model.account.Account;
import com.sogou.upd.passport.model.account.AccountAuth;
import com.sogou.upd.passport.model.account.AccountConnect;
import com.sogou.upd.passport.model.account.query.AccountConnectQuery;
import com.sogou.upd.passport.oauth2.authzserver.response.OAuthASResponse;
import com.sogou.upd.passport.oauth2.common.OAuthError;
import com.sogou.upd.passport.oauth2.common.OAuthResponse;
import com.sogou.upd.passport.oauth2.openresource.response.OAuthSinaSSOTokenRequest;
import com.sogou.upd.passport.service.account.AccountAuthService;
import com.sogou.upd.passport.service.account.AccountConnectService;
import com.sogou.upd.passport.service.account.AccountService;
import com.sogou.upd.passport.service.account.generator.PassportIDGenerator;
import com.sogou.upd.passport.web.BaseConnectController;
import com.sogou.upd.passport.web.form.SinaSSOCallbackParams;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * SSO-SDK第三方登录授权回调接口
 * User: shipengzhi
 * Date: 13-3-24
 * Time: 下午12:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/v2/connect")
public class SSOLoginCallbackController extends BaseConnectController {

	@Inject
	private AccountService accountService;
	@Inject
	private AccountAuthService accountAuthService;
	@Inject
	private AccountConnectService accountConnectService;

	@RequestMapping(value = "/ssologin/sina", method = RequestMethod.POST)
	@ResponseBody
	public Object handleCallbackRedirect(HttpServletRequest req, HttpServletResponse res,
			SinaSSOCallbackParams sinaSSOParams) throws Exception {

		OAuthSinaSSOTokenRequest oauthRequest = new OAuthSinaSSOTokenRequest(req);

		int accountType = AccountTypeEnum.SINA.getValue();
		int clientId = sinaSSOParams.getClient_id();
		String instanceId = sinaSSOParams.getInstance_id();
		String connectUid = oauthRequest.getConnectUid();

		OAuthResponse response = null;
		AccountAuth accountAuth;

		// 获取第三方用户信息
		try {
			AccountConnectQuery query = buildAccountConnectQuery(connectUid, clientId);
			List<AccountConnect> accountConnectList = accountConnectService.listAccountConnectByQuery(query);
			AccountConnect oldAccountConnect = getAppointAppKeyAccountConnect(accountConnectList, clientId);

			long userId;
			if (oldAccountConnect == null) { // 此账号未授权当前应用
				if (CollectionUtils.isEmpty(accountConnectList)) { // 此账号未授权过任何应用 
					Account newAccount = accountService.initialConnectAccount(connectUid, getIp(req), accountType);
					if (newAccount == null) {
						response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
								.setError(OAuthError.Response.AUTHORIZE_FAIL).setErrorDescription("login fail")
								.buildJSONMessage();
						return response.getBody();
					}
					userId = newAccount.getId();
				} else { // 此账号已存在，只是未在当前应用登录 TODO 注意QQ的不同appid返回的uid不同
					userId = accountConnectList.get(0).getUserId();
				}
				// TODO 是否有必要并行初始化Account_Auth和Account_Connect？
				String passportId = PassportIDGenerator.generator(connectUid, accountType);
				accountAuth = accountAuthService.initialAccountAuth(userId, passportId, clientId, instanceId);
				AccountConnect newAccountConnect = buildAccountConnect(userId, clientId, accountType,
						AccountConnect.STUTAS_LONGIN, connectUid, oauthRequest.getAccessToken(),
						oauthRequest.getExpiresIn(), oauthRequest.getRefreshToken());
				boolean isInitalAccountConnect = accountConnectService.initialAccountConnect(newAccountConnect);
				if (accountAuth == null || !isInitalAccountConnect) {
					response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.Response.AUTHORIZE_FAIL).setErrorDescription("login fail")
							.buildJSONMessage();
					return response.getBody();
				}

			} else { // 此账号在当前应用第N次登录
				userId = oldAccountConnect.getUserId();
				// 更新当前应用的Account_Auth，处于安全考虑refresh_token和access_token重新生成
				String passportId = PassportIDGenerator.generator(connectUid, accountType);
				accountAuth = accountAuthService.updateAccountAuth(userId, passportId, clientId, instanceId);
				// 更新当前应用的Account_Connect
				AccountConnect accountConnect = buildAccountConnect(userId, clientId, accountType,
						AccountConnect.STUTAS_LONGIN, connectUid, oauthRequest.getAccessToken(),
						oauthRequest.getExpiresIn(), oauthRequest.getRefreshToken());
				boolean isUpdateAccountConnect = accountConnectService.updateAccountConnect(accountConnect);
				if (accountAuth == null || !isUpdateAccountConnect) {
					response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.Response.AUTHORIZE_FAIL).setErrorDescription("login fail")
							.buildJSONMessage();
					return response.getBody();
				}
			}
			// TODO 如何保证数据一致性，采用insertAndUpdate()？

		} catch (Exception e) {
			e.printStackTrace();
			response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError(OAuthError.Response.AUTHORIZE_FAIL).setErrorDescription("login fail").buildJSONMessage();
			return response.getBody();
		}
		response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accountAuth.getAccessToken()).setExpiresTime(accountAuth.getAccessValidTime())
				.setRefreshToken(accountAuth.getRefreshToken()).buildJSONMessage();
		return response.getBody();
	}

	private AccountConnectQuery buildAccountConnectQuery(String connectUid, int clientId) {
		AccountConnectQuery query = new AccountConnectQuery();
		query.setConnectUid(connectUid);
		query.setClientId(clientId);
		return query;
	}

	/*
	该账号是否在当前应用登录过
	 */
	private AccountConnect getAppointAppKeyAccountConnect(List<AccountConnect> accountConnectList, int clientId) {
		AccountConnect accountConnect = null;
		for (AccountConnect connect : accountConnectList) {
			if (clientId == connect.getClientId()) {
				accountConnect = connect;
				break;
			}
		}
		return accountConnect;
	}

}
