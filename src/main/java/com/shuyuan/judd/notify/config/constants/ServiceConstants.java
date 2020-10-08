package com.shuyuan.judd.notify.config.constants;

import com.shuyuan.judd.client.constants.GlobalConstants;

/**
 * Created by Kevin
 * description:
 * date: 2018/11/12 6:19 PM
 */
public class ServiceConstants extends GlobalConstants {
    /**
     * 子商户商户统一成功返回码
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 联动响应成功返回码
     */
    public static final String FUNDINOUT_SUCCESS_CODE = "0000";
    /**
     * 联动响应受理中
     */
    public static final String REFUND_PROCESS_CODE = "00462624";
    /**
     * 重复绑卡返回码
     */
    public static final String REPEAT_CARD_BIND = "00462204";

    public final static int LOGIN_TOKEN_EXPIRE = 60*60;//登陆的token保持1h

    public final static String LOGIN_TOKEN_KEY = "demo-loginToken";



}
