package com.sogou.upd.passport;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: shipengzhi
 * Date: 13-4-23
 * Time: 上午1:19
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config-test.xml")
public class BaseTest extends AbstractJUnit4SpringContextTests {
    protected static final String userid = "4e1ca6c7999d5898306a243fc3da1e8f";

    protected static final String password = "testtest1";

    protected static final String uniqname = "你好";

    protected static final int clientId = 1115;

    protected static final String serverSecret = "RBCqf6a448Wj5a8#KF&POL75*5GBQ5";

    protected static final String modifyIp = "10.1.164.160";

    protected static final String question = "测试啊，我是来测试的";

    protected static final String answer = "测试成功";
}
