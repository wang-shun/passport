package com.sogou.upd.passport.common.utils;

import com.sogou.upd.passport.common.result.*;
import com.sogou.upd.passport.common.result.Result;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA. User: mayan Date: 12-11-22 Time: 下午6:26 To change this template use
 * File | Settings | File Templates.
 */
@Ignore
@ContextConfiguration(locations = {"classpath:spring-config-image.xml"})
public class ImgTest extends AbstractJUnit4SpringContextTests {

    @Inject
    private PhotoUtils photoUtils;

    @Test
    public void test() {
            File file=new File("d:\\1.png");
            try {
                boolean flag=photoUtils.uploadImg(UUID.randomUUID().toString(), IOUtils.toByteArray(new FileInputStream(file)),"http://a2.itc.cn/passport/avatar/9/4c/4/ece78d5a416548es_1378111902014_130_130.jpg","1");
                System.out.println(flag);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
    }

    @Test
    public void testUploadDefaultAvatar() {
        File file=new File("C:\\Users\\xieyilun\\Desktop\\a.jpg");
        try {
            String imgName = "default_avatar";
            boolean flag = photoUtils.uploadImg(imgName, IOUtils.toByteArray(new FileInputStream(file)), null, "0");
            String imgURL = photoUtils.accessURLTemplate(imgName);
            System.out.println("imgURL:" + imgURL);
            Result getPhotoResult = photoUtils.obtainPhoto(imgURL, "30,50,180");
            String large_avatar = (String) getPhotoResult.getModels().get("img_180");
            String mid_avatar = (String) getPhotoResult.getModels().get("img_50");
            String tiny_avatar = (String) getPhotoResult.getModels().get("img_30");

            System.out.println("large_avatar:" + large_avatar);
            System.out.println("mid_avatar:" + mid_avatar);
            System.out.println("tiny_avatar:" + tiny_avatar);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        private static final String BROWSER_DEFAULT_LARGE_AVATAR_URL = "http://img01.sogoucdn.com/app/a/100140008/default_avatar";
//        private static final String BROWSER_DEFAULT_MID_AVATAR_URL = "http://img01.sogoucdn.com/app/a/100140007/default_avatar";
//        private static final String BROWSER_DEFAULT_TINY_AVATAR_URL = "http://img01.sogoucdn.com/app/a/100140006/default_avatar";
    }

}
