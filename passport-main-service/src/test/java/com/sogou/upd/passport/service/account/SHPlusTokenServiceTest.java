package com.sogou.upd.passport.service.account;

import com.sogou.upd.passport.BaseTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: shipengzhi
 * Date: 13-9-10
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class SHPlusTokenServiceTest extends BaseTest {

    @Autowired
    private SHPlusTokenService shPlusTokenService;

    /**
     * https://open.account.sohu.com/oauth2/token/?grant_type=heartbeat&client_id=30000004
     * &client_secret=59be99d1f5e957ba5a20e8d9b4d76df6&redirect_url=www.sohu.com&scope=all&username=shipengzhi1986@sogou.com
     * &refresh_token=7d91b7c1e68ab8ff51c252dd02611b4b5ffb542542144c521625123b339b54c6&instance_id=323906108&sid=1061601372
     * &h=E3C6A85CE88C48A1FC35EAC5DF7FE09B&r=0000&v=4.2.0.8850
     */
    @Test
    public void testVerifyShPlusRefreshToken() {
        int clientId = 1065;
        String instanceId = "323906108";
        String passportId = "shipengzhi1986@sogou.com";
        String refreshToken = "58cc24195472c1c25a378513b6ec7b94dbb290a9468b6d774bf0fc4706330e93";
        boolean errorResult = shPlusTokenService.verifyShPlusRefreshToken(passportId, clientId, instanceId, refreshToken);
        Assert.assertTrue(!errorResult);
        refreshToken = "58cc24195472c1c25a378523b6ec7b94dbb290a9468b6d774bf0fc4706330e93";
        boolean successResult = shPlusTokenService.verifyShPlusRefreshToken(passportId, clientId, instanceId, refreshToken);
        Assert.assertTrue(successResult);
    }

    /**
     * https://account.sogou.com/oauth2/resource/?client_id=30000004&scope=all&instance_id=1213178981&resource_type=full.get
     * &access_token=94875c31900ce878b995095ee911d9e950f59e81cf218cca25d42adcc3c6ade5&h=DF9BB5F023D9D0007F4EC6345416E8FE&r=2170&v=4.2.0.8850
     */
    /**
     * http://open.account.sohu.com/oauth2/resource/?scope=all&resource_type=full.get&client_id=30000004
     * &access_token=94875c31900ce878b995095ee911d9e950f59e81cf218cca25d42adcc3c6ade5&instance_id=1213178981
     */
    @Test
    public void testGetResourceByToken() {

        //http://open.account.sohu.com/oauth2/resource/?
        // scope=all&
        // resource_type=full.get&
        // client_id=30000004&
        // access_token=94875c31900ce878b995095ee911d9e950f59e81cf218cca25d42adcc3c6ade5&
        // instance_id=1213178981
        String instanceId = "1213178981";
        String accesstoken = "60f1cdd4c9e48495ae7e8865aa78b0ad0c2ea508c20c89a438364892481ce786";
        String scope = "all";
        String resource_type = "full.get";

        String json = shPlusTokenService.getResourceByToken(instanceId, accesstoken, scope, resource_type);
        System.out.println("json:" + json);
    }

}