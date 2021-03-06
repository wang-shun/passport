package com.sogou.upd.passport.web.account.action;

import com.google.common.base.Strings;
import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.model.useroperationlog.UserOperationLog;
import com.sogou.upd.passport.common.parameter.AccountDomainEnum;
import com.sogou.upd.passport.common.result.APIResultSupport;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.ErrorUtil;
import com.sogou.upd.passport.manager.account.AccountInfoManager;
import com.sogou.upd.passport.manager.account.SecureManager;
import com.sogou.upd.passport.manager.api.SHPPUrlConstant;
import com.sogou.upd.passport.manager.api.account.UserInfoApiManager;
import com.sogou.upd.passport.manager.api.account.form.UpdateUserUniqnameApiParams;
import com.sogou.upd.passport.manager.api.account.form.UploadAvatarParams;
import com.sogou.upd.passport.manager.app.ConfigureManager;
import com.sogou.upd.passport.manager.form.AccountInfoParams;
import com.sogou.upd.passport.manager.form.ObtainAccountInfoParams;
import com.sogou.upd.passport.web.BaseController;
import com.sogou.upd.passport.web.ControllerHelper;
import com.sogou.upd.passport.web.UserOperationLogUtil;
import com.sogou.upd.passport.web.account.form.CheckOrUpdateNickNameParams;
import com.sogou.upd.passport.web.annotation.LoginRequired;
import com.sogou.upd.passport.web.annotation.ResponseResultType;
import com.sogou.upd.passport.web.inteceptor.HostHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息相关 上传头像 修改昵称
 * User: mayan
 * Date: 13-8-7
 * Time: 下午2:18
 */
@Controller
@RequestMapping("/web")
public class AccountInfoAction extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoAction.class);

    @Autowired
    private UserInfoApiManager sgUserInfoApiManager;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private AccountInfoManager accountInfoManager;
    @Autowired
    private ConfigureManager configureManager;
    @Autowired
    private SecureManager secureManager;

    @RequestMapping(value = "/userinfo/checknickname", method = RequestMethod.GET)
    @ResponseBody
    public Object checkNickName(HttpServletRequest request, CheckOrUpdateNickNameParams checkOrUpdateNickNameParams) {
        Result result = new APIResultSupport(false);
        //参数验证
        String validateResult = ControllerHelper.validateParams(checkOrUpdateNickNameParams);
        if (!Strings.isNullOrEmpty(validateResult)) {
            result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
            result.setMessage(validateResult);
            return result.toString();
        }
        UpdateUserUniqnameApiParams updateUserUniqnameApiParams = new UpdateUserUniqnameApiParams();
        updateUserUniqnameApiParams.setUniqname(checkOrUpdateNickNameParams.getNickname());
        updateUserUniqnameApiParams.setClient_id(SHPPUrlConstant.APP_ID);

        //增加安全限制 ip+cookie 暂不做cookie限制，需要前端配合加。
//        String cookie = ServletUtil.getCookie(request, "uuidName");
        if (accountInfoManager.checkNickNameExistInBlackList(getIp(request), StringUtils.EMPTY)) {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_USERNAME_IP_INBLACKLIST);
        } else {
            result = sgUserInfoApiManager.checkUniqName(updateUserUniqnameApiParams);
        }

        //用于记录log
        UserOperationLog userOperationLog = new UserOperationLog("", String.valueOf(SHPPUrlConstant.APP_ID), result.getCode(), getIp(request));
        UserOperationLogUtil.log(userOperationLog);
        return result.toString();
    }

    /**
     * 获取用户信息
     * <p/>
     * 数据迁移前（全量数据+增量数据完成导入前）:非第三方账号用户昵称、头像信息 读取account_base_info表，用户其他信息通过调用搜狐api获取
     * <p/>
     * 目标:数据迁移后（全量数据+增量数据完成导入后）:非第三方账号用户昵称、头像信息 读取account_0~32表，用户其他信息读取account_info_0~32表
     *
     * @param request
     * @param params
     * @param model
     * @return
     */
    @RequestMapping(value = "/userinfo/getuserinfo", method = RequestMethod.GET)
    @LoginRequired(resultType = ResponseResultType.redirect)
    public String obtainUserinfo(HttpServletRequest request, ObtainAccountInfoParams params, Model model) {
        Result result = new APIResultSupport(false);
        if (hostHolder.isLogin()) {
            //参数验证
            String validateResult = ControllerHelper.validateParams(params);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                model.addAttribute("data", result.toString());
                return "/person/index";
            }
            String userId = hostHolder.getPassportId();
            //验证client_id是否存在
            int clientId = Integer.parseInt(params.getClient_id());
            if (!configureManager.checkAppIsExist(clientId)) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                model.addAttribute("data", result.toString());
                return "/person/index";
            }
            if (Strings.isNullOrEmpty(params.getFields())) {
                params.setFields("province,city,gender,birthday,fullname,personalid");
            }
            params.setUsername(userId);
            //获取用户信息
            result = accountInfoManager.getUserInfo(params);

            result.getModels().put("uniqname", accountInfoManager.getUniqName(params.getUsername(), clientId, true));
    
            processAvatarUrl(request, result);
            
            //用于记录log
            UserOperationLog userOperationLog = new UserOperationLog(userId, params.getClient_id(), result.getCode(), getIp(request));
            UserOperationLogUtil.log(userOperationLog);

            AccountDomainEnum domain = AccountDomainEnum.getAccountDomain(userId);
            if (result.isSuccess()) {
                if (domain == AccountDomainEnum.THIRD) {
                    result.setDefaultModel("disable", true);
                }
                model.addAttribute("data", result.toString());
                result.setMessage("获取个人信息成功");
                return "/person/index";
            }
        }
        return "redirect:/web/webLogin";
    }

    //设置或修改个人信息
    @RequestMapping(value = "/userinfo/update", method = RequestMethod.POST)
    @LoginRequired(resultType = ResponseResultType.redirect)
    @ResponseBody
    public String updateUserInfo(HttpServletRequest request, AccountInfoParams infoParams) {
        Result result = new APIResultSupport(false);

        if (hostHolder.isLogin()) {
            //参数验证
            String validateResult = ControllerHelper.validateParams(infoParams);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            //验证client_id是否存在
            int clientId = Integer.parseInt(infoParams.getClient_id());
            if (!configureManager.checkAppIsExist(clientId)) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                return result.toString();
            }
            String ip = getIp(request);
            String userId = hostHolder.getPassportId();
            infoParams.setUsername(userId);
            result = accountInfoManager.updateUserInfo(infoParams, ip);
            UserOperationLog userOperationLog = new UserOperationLog(userId, String.valueOf(infoParams.getClient_id()), result.getCode(), getIp(request));
            UserOperationLogUtil.log(userOperationLog);
        } else {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKLOGIN_FAILED);
        }
        return result.toString();
    }

    //头像上传
    @RequestMapping(value = "/userinfo/uploadavatar")
    @LoginRequired(resultType = ResponseResultType.redirect)
    @ResponseBody
    public Object uploadAvatar(HttpServletRequest request, UploadAvatarParams params) {
        Result result = new APIResultSupport(false);
        if (hostHolder.isLogin()) {
            //参数验证
            String validateResult = ControllerHelper.validateParams(params);
            if (!Strings.isNullOrEmpty(validateResult)) {
                result.setCode(ErrorUtil.ERR_CODE_COM_REQURIE);
                result.setMessage(validateResult);
                return result.toString();
            }
            //验证client_id是否存在
            int clientId = Integer.parseInt(params.getClient_id());
            if (!configureManager.checkAppIsExist(clientId)) {
                result.setCode(ErrorUtil.INVALID_CLIENTID);
                return result.toString();
            }

            String userId = hostHolder.getPassportId();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile multipartFile = (CommonsMultipartFile) multipartRequest.getFile("Filedata");
            byte[] byteArr = multipartFile.getBytes();
            result = accountInfoManager.uploadImg(byteArr, userId, "0", getIp(request));

            //用于记录log
            UserOperationLog userOperationLog = new UserOperationLog(userId, params.getClient_id(), result.getCode(), getIp(request));
            UserOperationLogUtil.log(userOperationLog);
        } else {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKLOGIN_FAILED);
        }
        return result.toString();
    }

    //头像上传
    @RequestMapping(value = "/userinfo/avatarurl", method = RequestMethod.GET)
    @LoginRequired(resultType = ResponseResultType.redirect)
    public String uploadAvatarurl(HttpServletRequest request, Model model) throws Exception {
        Result result = new APIResultSupport(false);

        if (hostHolder.isLogin()) {
            String userId = hostHolder.getPassportId();
            result = secureManager.queryAccountSecureInfo(userId, CommonConstant.SGPP_DEFAULT_CLIENTID, false);

            //用于记录log
            UserOperationLog userOperationLog = new UserOperationLog(userId, String.valueOf(SHPPUrlConstant.APP_ID), result.getCode(), getIp(request));
            UserOperationLogUtil.log(userOperationLog);

            AccountDomainEnum domain = AccountDomainEnum.getAccountDomain(userId);
            if (domain == AccountDomainEnum.THIRD) {
//                result.getModels().put("uniqname", accountInfoManager.getUniqName(userId, CommonConstant.SGPP_DEFAULT_CLIENTID));
                //TODO disable 作用是对于第三方账号，不显示安全信息tab
                result.setDefaultModel("disable", true);
            }
            model.addAttribute("data", result.toString());
        } else {
            result.setCode(ErrorUtil.ERR_CODE_ACCOUNT_CHECKLOGIN_FAILED);
        }
        return "/person/avatar";
    }

}
