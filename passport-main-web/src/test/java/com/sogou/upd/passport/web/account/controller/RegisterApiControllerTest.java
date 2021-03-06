package com.sogou.upd.passport.web.account.controller;

import com.sogou.upd.passport.common.CommonConstant;
import com.sogou.upd.passport.common.utils.JacksonJsonMapperUtil;
import com.sogou.upd.passport.manager.ManagerHelper;
import com.sogou.upd.passport.web.BaseActionTest;
import com.sogou.upd.passport.web.account.form.APIResultForm;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 14-4-21
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */
//@Ignore
public class RegisterApiControllerTest extends BaseActionTest {

    private static final String SG_BASE_PATH_URL = "http://localhost";
    private static final String SH_BASE_PATH_URL = "http://10.11.211.152:8090";

    //发送短信验证码
    private static final String mobile_reged = "13581695053";
    private static final String right_mobile = "13720064130";
    private static final String wrong_mobile = "13581x95053";
    //检查用户名是否存在
    private static final String right_sogou = "iamfortest@sogou.com";
    private static final String have_sogou = "amuissuper@sogou.com";
    private static final String right_mail = "amuissuper@163.com";
    private static final String have_mail = "loveerin9460@163.com";
    private static final String have_unactive_mail = "testliuliu@163.com";
    private static final String wrong_format_mail = "testjisjf_c.com.com.com";
    private static final String password = "111111";
    private static final String createip = "65.132.11.120";
    private static final String ru = "http://account.sogou.com";
    //搜狗账号、外域邮箱账号正式注册
    private static final String reg_sogou = "testliuling" + new Random().nextInt(1000) + "@sogou.com";
    private static final String reg_format_sogou1 = "er>?asf" + new Random().nextInt(2000) + "@sogou.com"; //含非法字符
    private static final String reg_format_sogou2 = "contact" + new Random().nextInt(2000) + "@sogou.com"; //含限制字符
    private static final String both_no_username_mail = "testmail" + new Random().nextInt(100) + "@163.com";
    //注册手机号，不需短信验证码
    private static final String reg_mobile = "13926415207";
    private static final String bind_mobile = "15737126381";
    private static final String reged_mobile = "13581695053";
    //注册手机号，需要短信验证码
    private static final String reged_mobile_capthca = "13581695053";
    private static final String wrong_capthca = "grtyr";
    private static final String right_capthca = "1q23";
    private static final int clientId = CommonConstant.SGPP_DEFAULT_CLIENTID;
    private static final String serverSecret = "4xoG%9>2Z67iL5]OdtBq$l#>DfW@TY";


    /**
     * 测试发送手机验证码
     *
     * @throws java.io.IOException
     */
    @Test
    public void testSendRegCaptchaExists() throws IOException {
        //手机号已经注册
        String apiUrl = "/internal/account/sendregcaptcha";
        Map<String, String> params_reged = getSendCaptchaParams(mobile_reged);
//        String expectStringOne = sendPost(SH_BASE_PATH_URL + apiUrl, params_reged);
        String expectStringOne = "{\"data\":{\"userid\":\"" + mobile_reged + "@sohu.com\"},\"statusText\":\"手机号已绑定其他账号\",\"status\":\"20225\"}";
        APIResultForm expectFormOne = JacksonJsonMapperUtil.getMapper().readValue(expectStringOne, APIResultForm.class);
        String actualStringOne = sendPost(SG_BASE_PATH_URL + apiUrl, params_reged);
        APIResultForm actualFormOne = JacksonJsonMapperUtil.getMapper().readValue(actualStringOne, APIResultForm.class);
        Assert.assertTrue(expectFormOne.equals(actualFormOne));

        //手机号格式错误
        Map<String, String> params_format_wrong = getSendCaptchaParams(wrong_mobile);
//        String expectStringTwo = sendPost(SH_BASE_PATH_URL + apiUrl, params_format_wrong);
        String expectStringTwo = "{\"data\":{},\"status\":\"10002\",\"statusText\":\"手机号格式不正确\"}";
        APIResultForm expectFormTwo = JacksonJsonMapperUtil.getMapper().readValue(expectStringTwo, APIResultForm.class);
        String actualStringTwo = sendPost(SG_BASE_PATH_URL + apiUrl, params_format_wrong);
        APIResultForm actualFormTwo = JacksonJsonMapperUtil.getMapper().readValue(actualStringTwo, APIResultForm.class);
        Assert.assertTrue(expectFormTwo.equals(actualFormTwo));

        //手机已经绑定了主账号
        Map<String, String> params_bind = getSendCaptchaParams(bind_mobile);
//        String expectStringThree = sendPost(SH_BASE_PATH_URL + apiUrl, params_right);
        String expectStringFour = "{\"statusText\":\"手机号已绑定其他账号\",\"data\":{\"userid\":\"loveerin@sogou.com\"},\"status\":\"20225\"}";
        APIResultForm expectFormFour = JacksonJsonMapperUtil.getMapper().readValue(expectStringFour, APIResultForm.class);
        String actualStringFour = sendPost(SG_BASE_PATH_URL + apiUrl, params_bind);
        APIResultForm actualFormFour = JacksonJsonMapperUtil.getMapper().readValue(actualStringFour, APIResultForm.class);
        Assert.assertTrue(expectFormFour.equals(actualFormFour));

        //正确发送验证码
        Map<String, String> params_right = getSendCaptchaParams(reg_mobile_capthca);
//        String expectStringThree = sendPost(SH_BASE_PATH_URL + apiUrl, params_right);
        String expectStringThree = "{\"data\":{},\"statusText\":\"验证码已发送至" + reg_mobile_capthca + "\",\"status\":\"0\"}";
        APIResultForm expectFormThree = JacksonJsonMapperUtil.getMapper().readValue(expectStringThree, APIResultForm.class);
        String actualStringThree = sendPost(SG_BASE_PATH_URL + apiUrl, params_right);
        APIResultForm actualFormThree = JacksonJsonMapperUtil.getMapper().readValue(actualStringThree, APIResultForm.class);
        Assert.assertTrue(expectFormThree.equals(actualFormThree));
    }


    /**
     * 检查用户是否存在
     *
     * @throws java.io.IOException
     */
    @Test
    public void testCheckUser() throws IOException {
        //手机号已经注册或绑定其它账号
        String apiUrl = "/internal/account/checkuser";
        Map<String, String> params_reged = getCheckUserParams(mobile_reged);
        String actualStringOne = sendPost(SG_BASE_PATH_URL + apiUrl, params_reged);
        APIResultForm actualFormOne = JacksonJsonMapperUtil.getMapper().readValue(actualStringOne, APIResultForm.class);
//        String expectStringOne = sendPost(SH_BASE_PATH_URL + apiUrl, params_reged);
        String expectStringOne = "{\"data\":{\"flag\":\"1\",\"userid\":\"" + mobile_reged + "@sohu.com\"},\"status\":\"20225\",\"statusText\":\"手机号已绑定其他账号\"}";
        APIResultForm expectFormOne = JacksonJsonMapperUtil.getMapper().readValue(expectStringOne, APIResultForm.class);
        Assert.assertTrue(expectFormOne.equals(actualFormOne));

        //手机用户不存在
        Map<String, String> params_not_phone = getCheckUserParams(right_mobile);
        String actualStringTwo = sendPost(SG_BASE_PATH_URL + apiUrl, params_not_phone);
        APIResultForm actualFormTwo = JacksonJsonMapperUtil.getMapper().readValue(actualStringTwo, APIResultForm.class);
//        String expectStringTwo = sendPost(SH_BASE_PATH_URL + apiUrl, params_not_phone);
        String expectStringTwo = "{\"data\":{},\"status\":\"0\",\"statusText\":\"\"}";
        APIResultForm expectFormTwo = JacksonJsonMapperUtil.getMapper().readValue(expectStringTwo, APIResultForm.class);
        Assert.assertTrue(expectFormTwo.getStatus().equals(actualFormTwo.getStatus()));

        //搜狗用户不存在
        Map<String, String> params_not_sogou = getCheckUserParams(right_sogou);
//        String expectStringThree = sendPost(SH_BASE_PATH_URL + apiUrl, params_not_sogou);
        String expectStringThree = "{\"data\":{},\"status\":\"0\",\"statusText\":\"操作成功\"}";
        APIResultForm expectFormThree = JacksonJsonMapperUtil.getMapper().readValue(expectStringThree, APIResultForm.class);
        String actualStringThree = sendPost(SG_BASE_PATH_URL + apiUrl, params_not_sogou);
        APIResultForm actualFormThree = JacksonJsonMapperUtil.getMapper().readValue(actualStringThree, APIResultForm.class);
        Assert.assertTrue(expectFormThree.equals(actualFormThree));

        //搜狗用户已存在
        Map<String, String> params_have_sogou = getCheckUserParams(have_sogou);
//        String expectStringFour = sendPost(SH_BASE_PATH_URL + apiUrl, params_have_sogou);
        String expectStringFour = "{\"data\":{\"flag\":\"1\",\"userid\":\"" + have_sogou + "\"},\"status\":\"20294\",\"statusText\":\"用户名已经存在\"}";
        APIResultForm expectFormFour = JacksonJsonMapperUtil.getMapper().readValue(expectStringFour, APIResultForm.class);
        String actualStringFour = sendPost(SG_BASE_PATH_URL + apiUrl, params_have_sogou);
        APIResultForm actualFormFour = JacksonJsonMapperUtil.getMapper().readValue(actualStringFour, APIResultForm.class);
        Assert.assertTrue(expectFormFour.equals(actualFormFour));

        //外域邮箱用户不存在
        Map<String, String> params_not_mail = getCheckUserParams(right_mail);
//        String expectStringFive = sendPost(SH_BASE_PATH_URL + apiUrl, params_not_mail);
        String expectStringFive = "{\"data\":{},\"status\":\"0\",\"statusText\":\"操作成功\"}";
        APIResultForm expectFormFive = JacksonJsonMapperUtil.getMapper().readValue(expectStringFive, APIResultForm.class);
        String actualStringFive = sendPost(SG_BASE_PATH_URL + apiUrl, params_not_mail);
        APIResultForm actualFormFive = JacksonJsonMapperUtil.getMapper().readValue(actualStringFive, APIResultForm.class);
        Assert.assertTrue(expectFormFive.equals(actualFormFive));

        //外域邮箱用户已存在且已激活
        Map<String, String> params_have_mail = getCheckUserParams(have_mail);
//        String expectStringSix = sendPost(SH_BASE_PATH_URL + apiUrl, params_have_mail);
        String expectStringSix = "{\"data\":{\"flag\":\"1\",\"userid\":\"" + have_mail + "\"},\"status\":\"20294\",\"statusText\":\"用户名已经存在\"}";
        APIResultForm expectFormSix = JacksonJsonMapperUtil.getMapper().readValue(expectStringSix, APIResultForm.class);
        String actualStringSix = sendPost(SG_BASE_PATH_URL + apiUrl, params_have_mail);
        APIResultForm actualFormSix = JacksonJsonMapperUtil.getMapper().readValue(actualStringSix, APIResultForm.class);
        Assert.assertTrue(expectFormSix.equals(actualFormSix));

        //外域邮箱用户已存在但未激活
        Map<String, String> params_have_unactive_mail = getCheckUserParams(have_unactive_mail);
//        String expectStringSeven = sendPost(SH_BASE_PATH_URL + apiUrl, params_have_unactive_mail);
        String expectStringSeven = "{\"data\":{\"flag\":\"0\",\"userid\":\"" + have_unactive_mail + "\"},\"status\":\"20294\",\"statusText\":\"用户名已经存在\"}";
        APIResultForm expectFormSeven = JacksonJsonMapperUtil.getMapper().readValue(expectStringSeven, APIResultForm.class);
        String actualStringSeven = sendPost(SG_BASE_PATH_URL + apiUrl, params_have_unactive_mail);
        APIResultForm actualFormSeven = JacksonJsonMapperUtil.getMapper().readValue(actualStringSeven, APIResultForm.class);
        Assert.assertTrue(expectFormSeven.equals(actualFormSeven));

        //账户格式错误
        Map<String, String> params_wrong_format = getCheckUserParams(wrong_format_mail);
//        String expectStringEight = sendPost(SH_BASE_PATH_URL + apiUrl, params_wrong_format);
        String expectStringEight = "{\"data\":{\"userid\":\"" + wrong_format_mail + "\"},\"status\":\"20239\",\"statusText\":\"非法userid\"}";
        APIResultForm expectFormEight = JacksonJsonMapperUtil.getMapper().readValue(expectStringEight, APIResultForm.class);
        String actualStringEight = sendPost(SG_BASE_PATH_URL + apiUrl, params_wrong_format);
        APIResultForm actualFormEight = JacksonJsonMapperUtil.getMapper().readValue(actualStringEight, APIResultForm.class);
        Assert.assertTrue(expectFormEight.equals(actualFormEight));
    }

    /**
     * 正式注册接口--搜狗和外域邮箱账号
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRegMailUser() throws IOException {
        //搜狗账号正式注册
        String apiUrl = "/internal/account/reguser";
        Map<String, String> params_reg_sogou = getRegEmailApiParams(reg_sogou, password, createip, ru, "0");
        String actualStringOne = sendPost(SG_BASE_PATH_URL + apiUrl, params_reg_sogou);
        APIResultForm actualFormOne = JacksonJsonMapperUtil.getMapper().readValue(actualStringOne, APIResultForm.class);
        String expectStringOne = "{\"data\":{\"userid\":\"" + reg_sogou + "\",\"isSetCookie\":true},\"status\":\"0\",\"statusText\":\"注册成功\"}";
        APIResultForm expectFormOne = JacksonJsonMapperUtil.getMapper().readValue(expectStringOne, APIResultForm.class);
        Assert.assertTrue(expectFormOne.equals(actualFormOne));

        //外域邮箱账号正式注册
        Map<String, String> params_reg_mail = getRegEmailApiParams(both_no_username_mail, password, createip, ru, "1");
        String actualStringTwo = sendPost(SG_BASE_PATH_URL + apiUrl, params_reg_mail);
        APIResultForm actualFormTwo = JacksonJsonMapperUtil.getMapper().readValue(actualStringTwo, APIResultForm.class);
        String expectStringTwo = "{\"data\":{\"userid\":\"" + both_no_username_mail + "\",\"isSetCookie\":false},\"status\":\"0\",\"statusText\":\"注册成功\"}";
        APIResultForm expectFormTwo = JacksonJsonMapperUtil.getMapper().readValue(expectStringTwo, APIResultForm.class);
        Assert.assertTrue(expectFormTwo.equals(actualFormTwo));

        //搜狗账号正式注册账号非法(含非法字符)
        Map<String, String> params_format_sogou = getRegEmailApiParams(reg_format_sogou1, password, createip, ru, "0");
        String actualStringThree = sendPost(SG_BASE_PATH_URL + apiUrl, params_format_sogou);
        APIResultForm actualFormThree = JacksonJsonMapperUtil.getMapper().readValue(actualStringThree, APIResultForm.class);
        String expectStringThree = "{\"data\":{},\"status\":\"20239\",\"statusText\":\"非法userid\"}";
        APIResultForm expectFormThree = JacksonJsonMapperUtil.getMapper().readValue(expectStringThree, APIResultForm.class);
        Assert.assertTrue(expectFormThree.equals(actualFormThree));

        //搜狗账号正式注册账号非法(含限制字符)
        Map<String, String> params_format_sogou2 = getRegEmailApiParams(reg_format_sogou2, password, createip, ru, "0");
        String actualStringFour = sendPost(SG_BASE_PATH_URL + apiUrl, params_format_sogou2);
        APIResultForm actualFormFour = JacksonJsonMapperUtil.getMapper().readValue(actualStringFour, APIResultForm.class);
        String expectStringFour = "{\"data\":{},\"status\":\"20239\",\"statusText\":\"非法userid\"}";
        APIResultForm expectFormFour = JacksonJsonMapperUtil.getMapper().readValue(expectStringFour, APIResultForm.class);
        Assert.assertTrue(expectFormFour.equals(actualFormFour));

    }

    /**
     * 正式注册接口--手机账号（不需短信验证码）
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRegMobileUser() throws IOException {
        //手机号未被注册
        String apiUrl = "/internal/account/regmobile";
        Map<String, String> params_reg_mobile = getRegMobileApiParams(reg_mobile, password);
        String actualString = sendPost(SG_BASE_PATH_URL + apiUrl, params_reg_mobile);
        APIResultForm actualForm = JacksonJsonMapperUtil.getMapper().readValue(actualString, APIResultForm.class);
        String expectString = "{\"data\":{\"userid\":\"" + reg_mobile + "@sohu.com\",\"isSetCookie\":false},\"status\":\"0\",\"statusText\":\"注册成功\"}";
        APIResultForm expectForm = JacksonJsonMapperUtil.getMapper().readValue(expectString, APIResultForm.class);
        Assert.assertTrue(expectForm.equals(actualForm));

        //手机号已被注册或绑定
        Map<String, String> params_reged_mobile = getRegMobileApiParams(reged_mobile, password);
        String actualString2 = sendPost(SG_BASE_PATH_URL + apiUrl, params_reged_mobile);
        APIResultForm actualForm2 = JacksonJsonMapperUtil.getMapper().readValue(actualString2, APIResultForm.class);
        String expectString2 = "{\"data\":{},\"status\":\"20201\",\"statusText\":\"账号已注册\"}";
        APIResultForm expectForm2 = JacksonJsonMapperUtil.getMapper().readValue(expectString2, APIResultForm.class);
        Assert.assertTrue(expectForm2.equals(actualForm2));
    }

    /**
     * 正式注册接口--手机账号（需要短信验证码）
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRegMobileCaptchaUser() throws IOException {
        //手机号未被注册
        String apiUrl = "/internal/account/regmobileuser";
        Map<String, String> params_reg_mobile = getRegMobileCaptchaApiParams(reg_mobile_capthca, password, right_capthca, createip);
        String actualString = sendPost(SG_BASE_PATH_URL + apiUrl, params_reg_mobile);
        APIResultForm actualForm = JacksonJsonMapperUtil.getMapper().readValue(actualString, APIResultForm.class);
        String expectString = "{\"data\":{\"userid\":\"" + reg_mobile_capthca + "@sohu.com\",\"isSetCookie\":false},\"status\":\"0\",\"statusText\":\"注册成功\"}";
        APIResultForm expectForm = JacksonJsonMapperUtil.getMapper().readValue(expectString, APIResultForm.class);
        Assert.assertTrue(expectForm.equals(actualForm));

        //手机号已被注册或绑定
        Map<String, String> params_reged_mobile = getRegMobileCaptchaApiParams(reged_mobile_capthca, password, right_capthca, createip);
        String actualString2 = sendPost(SG_BASE_PATH_URL + apiUrl, params_reged_mobile);
        APIResultForm actualForm2 = JacksonJsonMapperUtil.getMapper().readValue(actualString2, APIResultForm.class);
        String expectString2 = "{\"data\":{},\"status\":\"20201\",\"statusText\":\"账号已注册\"}";
        APIResultForm expectForm2 = JacksonJsonMapperUtil.getMapper().readValue(expectString2, APIResultForm.class);
        Assert.assertTrue(expectForm2.equals(actualForm2));

        //验证码错误
        Map<String, String> params_captcha_wrong = getRegMobileCaptchaApiParams(reg_mobile_capthca, password, wrong_capthca, createip);
        String actualString3 = sendPost(SG_BASE_PATH_URL + apiUrl, params_captcha_wrong);
        APIResultForm actualForm3 = JacksonJsonMapperUtil.getMapper().readValue(actualString3, APIResultForm.class);
        String expectString3 = "{\"data\":{},\"status\":\"20208\",\"statusText\":\"验证码错误或已过期\"}";
        APIResultForm expectForm3 = JacksonJsonMapperUtil.getMapper().readValue(expectString3, APIResultForm.class);
        Assert.assertTrue(expectForm3.equals(actualForm3));
    }


    //构造发送手机验证码接口参数
    private Map<String, String> getSendCaptchaParams(String username) {
        Map<String, String> params = new HashMap<>();
        long ct = System.currentTimeMillis();
        String code = ManagerHelper.generatorCodeGBK(username, clientId, serverSecret, ct);
        params.put("mobile", username);
        params.put("client_id", String.valueOf(clientId));
        params.put("ct", String.valueOf(ct));
        params.put("code", code);
        return params;
    }

    //构造检查用户是否存在接口参数
    private Map<String, String> getCheckUserParams(String username) {
        Map<String, String> params = new HashMap<>();
        long ct = System.currentTimeMillis();
        String code = ManagerHelper.generatorCodeGBK(username, clientId, serverSecret, ct);
        params.put("userid", username);
        params.put("client_id", String.valueOf(clientId));
        params.put("ct", String.valueOf(ct));
        params.put("code", code);
        return params;
    }

    //构造注册搜狗账号和外域邮箱接口参数
    private Map<String, String> getRegEmailApiParams(String username, String password, String createip, String ru, String send_email) {
        Map<String, String> params = new HashMap<>();
        long ct = System.currentTimeMillis();
        String code = ManagerHelper.generatorCodeGBK(username, clientId, serverSecret, ct);
        params.put("userid", username);
        params.put("client_id", String.valueOf(clientId));
        params.put("ct", String.valueOf(ct));
        params.put("code", code);
        params.put("password", password);
        params.put("createip", createip);
        params.put("ru", ru);
        params.put("send_email", send_email);
        return params;
    }

    //构造注册手机账号接口参数（无需手机短信验证码）
    private Map<String, String> getRegMobileApiParams(String mobile, String password) {
        Map<String, String> params = new HashMap<>();
        long ct = System.currentTimeMillis();
        String code = ManagerHelper.generatorCodeGBK(mobile, clientId, serverSecret, ct);
        params.put("mobile", mobile);
        params.put("client_id", String.valueOf(clientId));
        params.put("ct", String.valueOf(ct));
        params.put("code", code);
        params.put("password", password);
        return params;
    }

    //构造注册手机账号接口参数（需要手机短信验证码）
    private Map<String, String> getRegMobileCaptchaApiParams(String mobile, String password, String captcha, String ip) {
        Map<String, String> params = new HashMap<>();
        long ct = System.currentTimeMillis();
        String code = ManagerHelper.generatorCodeGBK(mobile, clientId, serverSecret, ct);
        params.put("mobile", mobile);
        params.put("client_id", String.valueOf(clientId));
        params.put("ct", String.valueOf(ct));
        params.put("code", code);
        params.put("password", password);
        params.put("captcha", captcha);
        params.put("ip", ip);
        return params;
    }
}
