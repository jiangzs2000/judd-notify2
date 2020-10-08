package com.shuyuan.judd.notify.controller;

import org.junit.Test;
import com.shuyuan.judd.notify.BaseIntegrationTest;
import spring.shuyuan.judd.base.utils.ZipUtils;

/**
 * @author Sting
 * create 2018/11/22

 **/
public class ZipCompressTest extends BaseIntegrationTest {

    @Test
    public void zipCompress() throws Exception {
//        ZipUtils.zipFiles("D:\\app\\financefinance\\finance\\commerical_one","D:\\app\\financefinance\\finance\\commerical_one.zip");
        ZipUtils.zipSingleDirectory("D:\\app\\financefinance\\finance\\commerical_one","D:\\app\\financefinance\\finance\\commerical_one.zip");
    }
}

