package com.zhaihuilin.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zhaihuilin on 2017/11/14  15:34.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class SmsCode implements Serializable {

    @Id
    @GeneratedValue(generator = "")
    private Integer smsId;
    private String mtMsgId;             //短信平台反馈的短信唯一标识
    private String sa;                  //源手机号
    @NonNull
    private String phone;               //买家手机号
    @NonNull
    private String smsCode;         //短信验证码
    private long requestTime;       //请求短信时间
    private String state;               //其值为ACCEPTD表示MLINK端成功接收到SP端的请求，其他值时表示MLINK端接收SP端请求失败。
    private String errCode;          //其值为000，表示无错误返回，其他值时表示MLINK端接收SP端请求失败
    private Timestamp updateTime;        //短信状态反馈更新时间
}
