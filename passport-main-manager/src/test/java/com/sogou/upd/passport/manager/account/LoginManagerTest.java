package com.sogou.upd.passport.manager.account;

import com.sogou.upd.passport.BaseTest;
import com.sogou.upd.passport.common.result.APIResultForm;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.common.utils.JacksonJsonMapperUtil;
import com.sogou.upd.passport.manager.form.WebLoginParams;
import junit.framework.Assert;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: ligang201716@sogou-inc.com
 * Date: 13-6-7
 * Time: 下午2:02
 */
public class LoginManagerTest extends BaseTest {

    @Autowired
    private LoginManager loginManager;


    private static final int clientId = 1100;
    private static final String username = "18607369478";
    private static final String ip = "192.168.226.174";
    private static final String pwd = "654321";
    private static final String scheme = "http";
    private static final String test_mail = "testliuliu@163.com";


    private static final String username_waiyu = "tinkame@126.com";
    private static final String pwd_waiyu = "123456";

    private static final String username_phone = "13581695053";
    private static final String passportId_phone = "13581695053@sohu.com";
    private static final String pwd_phone = "111111";

    private static final String username_sogou = "tinkame732@sogou.com";
    private static final String pwd_sogou = "111111";

    @Test
    public void testAccountLogin() {
        try {
            WebLoginParams webLoginParams3 = getParam(username_waiyu, pwd_waiyu);
            Result result_email = loginManager.accountLogin(webLoginParams3, ip, scheme);
            APIResultForm email_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_email.toString(), APIResultForm.class);
            String expire_email_data = "{\"data\":{\"userid\":\"" + username_waiyu + "\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expireResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_email_data, APIResultForm.class);
            Assert.assertTrue(expireResultForm.equals(email_APIResultForm));


            WebLoginParams webLoginParams2 = getParam(passportId_phone, pwd_phone);
            Result result_soji = loginManager.accountLogin(webLoginParams2, ip, scheme);
            String expire_phone_data = "{\"data\":{\"userid\":\"" + passportId_phone + "\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm phone_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_soji.toString(), APIResultForm.class);
            APIResultForm expire_phone_ResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_phone_data, APIResultForm.class);
            Assert.assertTrue(expire_phone_ResultForm.equals(phone_APIResultForm));

            WebLoginParams webLoginParams1 = getParam(username_sogou, pwd_sogou);
            Result result_sogou = loginManager.accountLogin(webLoginParams1, ip, scheme);
            String expire_sogou_data = "{\"data\":{},\"status\":\"20206\",\"statusText\":\"密码错误\"}";
            APIResultForm sogou_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_sogou.toString(), APIResultForm.class);
            APIResultForm expire_sogou_ResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_sogou_data, APIResultForm.class);
            Assert.assertTrue(expire_sogou_ResultForm.equals(sogou_APIResultForm));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthUser_Success() {
        try {

            //外域邮箱密码验证成功
            String pwdMD5_email = DigestUtils.md5Hex(pwd_waiyu.getBytes());
            Result result_email = loginManager.authUser(username_waiyu, ip, pwdMD5_email);
            APIResultForm email_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_email.toString(), APIResultForm.class);
            String expire_email_data = "{\"data\":{\"userid\":\"" + username_waiyu + "\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expireEmailResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_email_data, APIResultForm.class);
            Assert.assertTrue(expireEmailResultForm.equals(email_APIResultForm));

            //单纯注册手机号可登录成功
            String pwdMD5_phone = DigestUtils.md5Hex(pwd_phone.getBytes());
            Result result_phone = loginManager.authUser(username_phone, ip, pwdMD5_phone);
            APIResultForm phone_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_phone.toString(), APIResultForm.class);
            String expire_phone_data = "{\"data\":{\"userid\":\"" + username_phone + "@sohu.com\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expirePhoneResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_phone_data, APIResultForm.class);
            Assert.assertTrue(expirePhoneResultForm.equals(phone_APIResultForm));

            //注册手机号+@sohu.com密码验证成功
            String pwdMD5_phone_pp = DigestUtils.md5Hex(pwd_phone.getBytes());
            Result result_phone_pp = loginManager.authUser(passportId_phone, ip, pwdMD5_phone_pp);
            APIResultForm phone__pp_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_phone_pp.toString(), APIResultForm.class);
            String expire_phone_pp_data = "{\"data\":{\"userid\":\"" + passportId_phone + "\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expirePhonePPResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_phone_pp_data, APIResultForm.class);
            Assert.assertTrue(expirePhonePPResultForm.equals(phone__pp_APIResultForm));

            //个性账号密码验证成功
            String pwdMD5_sogou = DigestUtils.md5Hex(pwd_sogou.getBytes());
            Result result_sogou = loginManager.authUser(username_sogou, ip, pwdMD5_sogou);
            APIResultForm sogou_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_sogou.toString(), APIResultForm.class);
            String expire_sogou_data = "{\"data\":{\"userid\":\"" + username_sogou + "\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expireSogouResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_sogou_data, APIResultForm.class);
            Assert.assertTrue(expireSogouResultForm.equals(sogou_APIResultForm));

            //单纯个性账号不加@sogou.com可也登录成功
            String pwdMD5_gexing = DigestUtils.md5Hex(pwd_sogou.getBytes());
            Result result_gexing = loginManager.authUser("tinkame732", ip, pwdMD5_gexing);
            APIResultForm gexing_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_gexing.toString(), APIResultForm.class);
            String expire_gexing_data = "{\"data\":{\"userid\":\"tinkame732@sogou.com\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expireGexingResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_gexing_data, APIResultForm.class);
            Assert.assertTrue(expireGexingResultForm.equals(gexing_APIResultForm));

            //绑定手机不加@sohu.com可也登录成功
            String pwdMD5_bind_mobile = DigestUtils.md5Hex(pwd_sogou.getBytes());
            Result result_bind_mobile = loginManager.authUser(mobile_1, ip, pwdMD5_bind_mobile);
            APIResultForm bind_mobile_APIResultForm = JacksonJsonMapperUtil.getMapper().readValue(result_bind_mobile.toString(), APIResultForm.class);
            String expire_bind_mobile_data = "{\"data\":{\"userid\":\"loveerin@sogou.com\"},\"status\":\"0\",\"statusText\":\"操作成功\"}";
            APIResultForm expireBindMobileResultForm = JacksonJsonMapperUtil.getMapper().readValue(expire_bind_mobile_data, APIResultForm.class);
            Assert.assertTrue(expireBindMobileResultForm.equals(bind_mobile_APIResultForm));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthUser_Error() {
        try {
            //用户名密码不匹配
            String pwdMD51 = DigestUtils.md5Hex(pwd.getBytes());
            Result result1 = loginManager.authUser(username_waiyu, ip, pwdMD51);
            APIResultForm APIResultForm1 = JacksonJsonMapperUtil.getMapper().readValue(result1.toString(), APIResultForm.class);
            String expire_data1 = "{\"statusText\":\"密码错误\",\"data\":{},\"status\":\"20206\"}";
            APIResultForm expireResultForm1 = JacksonJsonMapperUtil.getMapper().readValue(expire_data1, APIResultForm.class);
            Assert.assertTrue(expireResultForm1.equals(APIResultForm1));
            //外域用户在搜狗这边没有，在搜狐那边未激活时
            String pwdMD52 = DigestUtils.md5Hex("111111".getBytes());
            Result result2 = loginManager.authUser(test_mail, ip, pwdMD52);
            APIResultForm APIResultForm2 = JacksonJsonMapperUtil.getMapper().readValue(result2.toString(), APIResultForm.class);
            String expire_data2 = "{\"statusText\":\"账号未激活\",\"data\":{\"createtime\":\"1398246072000\",\"token\":\"699d40bde1cf0809ea6740497e780687\",\"activateurl\":\"https://passport.sohu.com/web/remind_activate.jsp\"},\"status\":\"20231\"}";
            APIResultForm expireResultForm2 = JacksonJsonMapperUtil.getMapper().readValue(expire_data2, APIResultForm.class);
            Assert.assertTrue(expireResultForm2.getStatus().equals(APIResultForm2.getStatus()));
            //手机号码没有绑定
            String pwdMD53 = DigestUtils.md5Hex("111111".getBytes());
            Result result3 = loginManager.authUser(username, ip, pwdMD53);
            System.out.println(result3.toString());
            APIResultForm APIResultForm3 = JacksonJsonMapperUtil.getMapper().readValue(result3.toString(), APIResultForm.class);
            String expire_data3 = "{\"statusText\":\"手机号未绑定账号\",\"data\":{},\"status\":\"20233\"}";
            APIResultForm expireResultForm3 = JacksonJsonMapperUtil.getMapper().readValue(expire_data3, APIResultForm.class);
            Assert.assertTrue(expireResultForm3.equals(APIResultForm3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private WebLoginParams getParam(String username, String password) {
        WebLoginParams webLoginParams = new WebLoginParams();
        webLoginParams.setClient_id("1100");
        webLoginParams.setUsername(username);
        webLoginParams.setPassword(password);
        return webLoginParams;
    }

}
