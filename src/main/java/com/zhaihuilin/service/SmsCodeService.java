package com.zhaihuilin.service;

import com.zhaihuilin.entity.SmsCode;

/**
 * 短信接口
 * Created by zhaihuilin on 2017/11/14  15:49.
 */
public interface SmsCodeService {

    public  SmsCode findSmsCodeByMtMsgId(String mtMsgId);

    public  SmsCode findSmsCodeByPhoneAndRequestTime(String phone,long requestTime);

    public  SmsCode updateSmsCodeByMtMsgId(SmsCode smsCode);

    public  SmsCode saveSmsCode(SmsCode smsCode);


}
