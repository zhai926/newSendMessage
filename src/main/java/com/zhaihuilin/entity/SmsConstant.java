package com.zhaihuilin.entity;

import com.zhaihuilin.util.GenerateSmsUtils;

/**
 * 发送短信工具
 * Created by zhaihuilin on 2017/11/14  15:57.
 */
public class SmsConstant {


    //账号
    public static final String SPID = "9870";
    //密码
    public static final String SPPASSWORD = "GrTEsQE0";
    //申请发送短信url
    public static final String MTURL = "http://esms100.10690007.net/sms/mt";
    //请求指令标志本应用向短信平台发送请求
    public static final String COMMAND_REQUEST = "MT_REQUEST";
    //请求指令标志本应用向短信平台发送响应
    public static final String COMMAND_RESPONSE = "MT_RESPONSE";

    //mtstat(接收请求成功标志)
    public static final String MTSTAT_ACCEPTD = "ACCEPTD";

    //moerrcode
    public static final String ERRCODE_SUCCEED = "000";
    //源号码
    public static final String SA = "10657109053657";
    //目标号码前缀
    public static final String DA_sufix = "86";
    //短信验证码有效期(15分钟)
    public static final long AVAILABLE_PERIOD = 15;
    //短信验证码内容前缀
    public static final String L_CONTANT = "您好！,此次你的手机验证码是:";
    //短信验证码内容后缀
    public static final String R_CONTANT = "请在15分钟内使用！谢谢！";
    //短信验证码内容(只用于测试)
    public static final String CONTANT = L_CONTANT.concat(GenerateSmsUtils.generateSms().concat(R_CONTANT));

}
