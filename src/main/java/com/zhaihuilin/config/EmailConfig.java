package com.zhaihuilin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhaihuilin on 2017/11/14  13:14.
 */
@Configuration
public class EmailConfig {

    @Value("http://localhost:8080/member/updatePwd")
    public String URL;

    //管理员邮箱  此处随便写一个邮箱
    @Value("111@qq.com")
    public String MEMEMAIL;
}
