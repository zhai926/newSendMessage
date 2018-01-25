package com.zhaihuilin.service.impl;

import com.zhaihuilin.dao.SmsCodeRepository;
import com.zhaihuilin.entity.SmsCode;
import com.zhaihuilin.service.SmsCodeService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhaihuilin on 2017/11/14  15:50.
 */
@Service
@Log4j
public class SmsCodeServiceImpl implements SmsCodeService {

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Override
    public SmsCode findSmsCodeByMtMsgId(String mtMsgId) {
        return smsCodeRepository.findSmsCodeByMtMsgId(mtMsgId);
    }

    @Override
    public SmsCode findSmsCodeByPhoneAndRequestTime(String phone, long requestTime) {
        return smsCodeRepository.findSmsCodeByPhoneAndRequestTime(phone,requestTime);
    }

    @Override
    public SmsCode updateSmsCodeByMtMsgId(SmsCode smsCode) {
        return smsCodeRepository.updateSmsCodeByMtMsgId(smsCode);
    }

    @Override
    public SmsCode saveSmsCode(SmsCode smsCode) {
        return smsCodeRepository.save(smsCode);
    }
}
