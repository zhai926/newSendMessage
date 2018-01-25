package com.zhaihuilin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by zhaihuilin on 2017/11/22  9:34.
 */
@Component
@Configuration
public class SmsToolConfig {

     @Value("17317900836")
     private   String PHONENUMBERS ; // 待发送的手机号码

     @Value("翟氏集团")
     private  String SIGNNAME;//短信签名

     @Value("SMS_112470565")
     private  String PHY_TEMPLATECODE;  //物流模板

     @Value("SMS_113090036")
     private  String PHY_TEMPLATECODE_COPY;  //物流副本模板

     @Value("LTAINP57QrqcuYWH")
     private  String  ACCESSKEY_ID;// 阿里云访问密钥ID

     @Value("YPPDDxsWEKXOy03O9TgXuvYHfFcv56")
     private  String  ACCESSKEY_SECRET;//  阿里云访问密钥SECRET

     @Value("Dysmsapi")
     private  String  PRODUCT;//产品名称:云通信短信API产品,开发者无需替换

     @Value("dysmsapi.aliyuncs.com")
     private  String  DOMAIN;//产品域名,开发者无需替换

}
