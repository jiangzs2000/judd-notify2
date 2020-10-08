package com.shuyuan.judd.notify.controller;

import com.shuyuan.judd.notify.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import spring.shuyuan.judd.base.utils.PictureDownloadUtils;

import java.io.File;

/**
 * @author Sting
 * create 2018/11/22

 **/
public class PictureDownloadTest extends BaseIntegrationTest {

    @Value("${ldlj.download.catalogue}")
    private String filePath;

    @Test
    public void downloadPicture() throws Exception {
//        PictureDownloadUtils.downloadPicture("http://aidangjia-oss.shuidihome.com/upload/2281574b07c0d2ce66f7f7012f1e7ab3.jpg", filePath, "hello.jpg");

//        PictureDownloadUtils.downloadPictures(new String[]{"http://aidangjia-oss.shuidihome.com/upload/be1c937fd6d3c06c1bf79485d4ca34da.jpg", "http://aidangjia-oss.shuidihome.com/upload/be1c937fd6d3c06c1bf79485d4ca34da.jpg"}, filePath + File.separator + "commerical_one", new String[]{"BUSSINESS_LICENSE.jpg", "BANK_ACCOUNT_LICENCE.jpg"});

        PictureDownloadUtils.downloadAndCompressPictures(new String[]{"http://aidangjia-oss.shuidihome.com/upload/be1c937fd6d3c06c1bf79485d4ca34da.jpg", "http://aidangjia-oss.shuidihome.com/upload/be1c937fd6d3c06c1bf79485d4ca34da.jpg"}, filePath + File.separator + "commerical_one", new String[]{"BUSSINESS_LICENSE.jpg", "BANK_ACCOUNT_LICENCE.jpg"},400);
    }
}
