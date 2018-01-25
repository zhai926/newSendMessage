package com.zhaihuilin.dao;

import com.zhaihuilin.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by zhaihuilin on 2017/11/14  15:39.
 */
@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode,Long>,JpaSpecificationExecutor<SmsCode> {

   public  SmsCode findSmsCodeByMtMsgId(String mtMsgId);

   public  SmsCode findSmsCodeByPhoneAndRequestTime(String phone,long requestTime);

   @Query(value = " update SmsCode s set s.updateTime=?2,s.state=?3,s.requestTime=?4,s.smsCode=?5,s.errCode=?6 where s.mtMsgId=?1")
   public  SmsCode updateSmsCodeByMtMsgId(SmsCode smsCode);



}
