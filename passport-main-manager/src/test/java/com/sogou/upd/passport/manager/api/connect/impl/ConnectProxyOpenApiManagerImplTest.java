package com.sogou.upd.passport.manager.api.connect.impl;

import com.sogou.upd.passport.BaseTest;
import com.sogou.upd.passport.common.result.Result;
import com.sogou.upd.passport.manager.api.connect.ConnectProxyOpenApiManager;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liuling
 * Date: 14-1-10
 * Time: 下午5:40
 * To change this template use File | Settings | File Templates.
 */
//@Ignore
@ContextConfiguration(locations = "classpath:spring-config-test.xml")
public class ConnectProxyOpenApiManagerImplTest extends BaseTest {
    @Autowired
    private ConnectProxyOpenApiManager connectProxyOpenApiManager;

    /**
     * 获取QQ空间未读数
     *
     * @throws java.io.IOException
     */
    @Test
    public void testQzoneConnectProxyOpenApiManager() throws Exception {
        //用户的openId/openKey
        String openId = "0000CC18B7AB275ABCA5873D7B816348";
        String accessToken = "C4EB82637FBE93196DDB868BD028B74C";
        int clientId = 1120;
        String sgUrl = "/internal/connect/qq/user/qzone/unread_num";
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("open_id", openId);
        tokenMap.put("access_token", accessToken);
        tokenMap.put("client_id", String.valueOf(clientId));
        Result result = connectProxyOpenApiManager.handleConnectOpenApi(sgUrl, tokenMap, null, null);
        System.out.println("-------------------------结果如下:--------------------------");
        System.out.println(result);
    }

    /**
     * 获取QQ微博未读数
     *
     * @throws java.io.IOException
     */
    @Test
    public void testWeiboConnectProxyOpenApiManager() throws Exception {
        //用户的openId/openKey
        String openId = "0000CC18B7AB275ABCA5873D7B816348";
        String accessToken = "C4EB82637FBE93196DDB868BD028B74C";
        int clientId = 1120;
        String sgUrl = "/internal/connect/qq/user/weibo/unread_num";
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("open_id", openId);
        tokenMap.put("access_token", accessToken);
        tokenMap.put("client_id", String.valueOf(clientId));
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("pf", "tapp");
        Result result = connectProxyOpenApiManager.handleConnectOpenApi(sgUrl, tokenMap, paramMap, null);
        System.out.println("-------------------------结果如下:--------------------------");
        System.out.println(result);
    }


    /**
     * 获取QQ邮箱未读数
     *
     * @throws java.io.IOException
     */
    @Test
    public void testMailConnectProxyOpenApiManager() throws Exception {
        //用户的openId/openKey
        String openId = "0000CC18B7AB275ABCA5873D7B816348";
        String accessToken = "C4EB82637FBE93196DDB868BD028B74C";
        String sgUrl = "/internal/connect/qq/user/mail/unread_num";
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("open_id", openId);
        tokenMap.put("access_token", accessToken);
        tokenMap.put("client_id", String.valueOf(clientId));
        Result result = connectProxyOpenApiManager.handleConnectOpenApi(sgUrl, tokenMap, null, null);
        System.out.println("-------------------------结果如下:--------------------------");
        System.out.println(result);
    }
}


