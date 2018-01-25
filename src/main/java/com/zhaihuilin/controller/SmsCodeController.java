package com.zhaihuilin.controller;

import com.zhaihuilin.entity.RequestState;
import com.zhaihuilin.entity.ReturnMessages;
import com.zhaihuilin.entity.SmsCode;
import com.zhaihuilin.entity.SmsConstant;
import com.zhaihuilin.service.SmsCodeService;
import com.zhaihuilin.util.GenerateSmsUtils;
import com.zhaihuilin.util.SmsSendUtils;
import com.zhaihuilin.util.StringUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaihuilin on 2017/11/14  16:07.
 */
@RestController
@Log4j
@RequestMapping(value = "/SmsCode")
public class SmsCodeController  {

    @Autowired
    private  SmsCodeService smsCodeService;

    @RequestMapping(value = "/sendSms")
    public ReturnMessages sendSms(
          @RequestParam(value = "phone",defaultValue = "",required = true) String phone,
          HttpServletRequest request
    ){
        ReturnMessages returnMessages=null;
        if (!StringUtils.isNotEmpty(phone)){
            return  new ReturnMessages(RequestState.ERROR,"电话不能为空！",null);
        }
        String Code=GenerateSmsUtils.generateSms();//生成随机产生的验证码
        String contant = phone.concat(SmsConstant.L_CONTANT.concat(Code.concat(SmsConstant.R_CONTANT)));
        HashMap<String, String> pp = (HashMap<String, String>) SmsSendUtils.singleMt(phone, contant);
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            if (pp !=null){
                SmsCode smsCode=new SmsCode();
                smsCode.setPhone(phone);
                smsCode.setRequestTime(new Date().getTime());
                smsCode.setSmsCode(Code);
                smsCode.setSa(SmsConstant.SA);
                smsCode.setMtMsgId(pp.get("mtmsgid"));
                smsCode.setState(pp.get("mtstat"));
                smsCode.setErrCode(pp.get("mterrcode"));
                smsCode=smsCodeService.saveSmsCode(smsCode);
                String timer = String.valueOf(new Date().getTime());
                HttpSession session=request.getSession();
                session.setAttribute(phone.concat(timer), Code);
                map.put("code", Code);
                map.put("timer", timer);
                if (smsCode !=null){
                    map.put("smsCode",smsCode);
                    return  new ReturnMessages(RequestState.SUCCESS,"发送成功！",map);
                }else {
                    return  new ReturnMessages(RequestState.ERROR,"发送失败！",null);
                }
            }
            return  new ReturnMessages(RequestState.ERROR,"发送失败！",null);
        }catch (Exception e){
            return  new ReturnMessages(RequestState.ERROR,"发送失败！",null);
        }
    }

}
