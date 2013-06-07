package com.sogou.upd.passport.service.account;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created with IntelliJ IDEA. User: hujunfei Date: 13-5-22 Time: 下午4:14 To change this template use
 * File | Settings | File Templates.
 */
@ContextConfiguration(locations = "classpath:spring-config-test.xml")
public class AccountSecureServiceTest extends AbstractJUnit4SpringContextTests {
    private static final String PASSPORT_ID = "13552848876@sohu.com";
    private static final int CLIENT_ID = 999;

    @Autowired
    AccountSecureService accountSecureService;

    @Test
    public void testGetAndSetSecureCodeResetPwd() {
        System.out.println("测试一");
        String secureCode = accountSecureService.getSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID);
        System.out.println("Secure Code is: " + secureCode);
        boolean checkRes = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID, secureCode);
        boolean checkRandom = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID, "ABC");
        System.out.println("验证结果是：" + checkRes + " _ " + checkRandom);
        checkRes = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID, secureCode);
        checkRandom = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID, "ABC");
        System.out.println("二次验证结果是：" + checkRes + " _ " + checkRandom);

    }

    @Test
    public void testGetAndSetSecureCode() {
        System.out.println("测试三");
        String secureCode = accountSecureService.getSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID);
        System.out.println("Secure Code is: " + secureCode);
        boolean checkRes = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID,
                                                                        secureCode);
        boolean checkRandom = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID,
                                                                                CLIENT_ID,
                                                                                secureCode);
        System.out.println("验证结果是：" + checkRes + " _ " + checkRandom);
        checkRes = accountSecureService.checkSecureCodeResetPwd(PASSPORT_ID, CLIENT_ID, secureCode);
        checkRandom = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID,
                                                                        secureCode);
        System.out.println("二次验证结果是：" + checkRes + " _ " + checkRandom);

    }

    @Test
    public void testGetAndSetSecureCodeMod() {
        System.out.println("测试二");
        String secureCode = accountSecureService.getSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID);
        System.out.println("Secure Code is: " + secureCode);
        boolean checkRes = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID,
                                                                             secureCode);
        boolean checkRandom = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID,
                                                                                CLIENT_ID, "ABC");
        System.out.println("验证结果是：" + checkRes + " _ " + checkRandom);
        checkRes = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID,
                                                                     secureCode);
        checkRandom = accountSecureService.checkSecureCodeModSecureInfo(PASSPORT_ID, CLIENT_ID,
                                                                        "ABC");
        System.out.println("二次验证结果是：" + checkRes + " _ " + checkRandom);

    }
}