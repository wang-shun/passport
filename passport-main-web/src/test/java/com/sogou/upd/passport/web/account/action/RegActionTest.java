package com.sogou.upd.passport.web.account.action;

import com.sogou.upd.passport.web.test.BaseActionTest;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: mayan Date: 13-6-25 Time: 下午5:33 To change this template use
 * File | Settings | File Templates.
 */
//@ContextConfiguration(locations = "classpath:spring-config-test.xml")
public class RegActionTest extends BaseActionTest {

    @Test
    public void testReg() throws IOException {
        String localUrl = "/web/reguser/";
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "1100");
        params.put("username", "dasdasdasdasdasd1");
        params.put("password", "asasasa");
        params.put("captcha", "GVD2X");
        String result  = sendPost("http://account.sogou.com/web/reguser", params);
        System.out.println(result);
    }



}