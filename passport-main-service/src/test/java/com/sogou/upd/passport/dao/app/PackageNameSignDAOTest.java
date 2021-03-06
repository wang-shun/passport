package com.sogou.upd.passport.dao.app;

import com.sogou.upd.passport.dao.BaseDAOTest;
import com.sogou.upd.passport.model.app.PackageNameSign;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created with IntelliJ IDEA. User: shipengzhi Date: 13-4-17 Time: 下午4:32 To change this template
 * use File | Settings | File Templates.
 */
@Ignore
public class PackageNameSignDAOTest extends BaseDAOTest {

    @Autowired
    private PackageNameSignDAO packageNameSignDAO;

    @Test
    public void testInsertPackageNameSign() {
        PackageNameSign packageNameSign = new PackageNameSign();
        packageNameSign.setClientId(1120);
        packageNameSign.setPackageName("com.sogou.passportsdk.demo");
        packageNameSign.setPackageSign("");
        Date date=new Date();
        packageNameSign.setCreateTime(date);
        packageNameSign.setUpdateTime(date);

        int num = packageNameSignDAO.insertPackageNameSign(packageNameSign);
        System.out.println("insert num:" + num);
    }

    @Test
    public void testGetPackageNameSign() {
        PackageNameSign packageNameSign = packageNameSignDAO.getPackageNameSignByname("sogou.passport");
        System.out.println("id:" + packageNameSign.getId()
                + ",clientId:" + packageNameSign.getClientId()
                + ",packageName:" + packageNameSign.getPackageName()
                + ",packageSign:" + packageNameSign.getPackageSign()
                + ",updateTime:" + packageNameSign.getUpdateTime()
                + ",createTime:" + packageNameSign.getCreateTime()
        );

    }


}
