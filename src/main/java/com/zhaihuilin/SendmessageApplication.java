package com.zhaihuilin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching  //开启缓存
@EnableRedisHttpSession     //开启 spring  session 支持
@EnableWebSecurity   // 开启权限认证   要求所有的url 请求必须在登录状态下
@ImportResource(locations={"classpath:Kaptcher.xml"})
@EnableTransactionManagement// 启动注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class SendmessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendmessageApplication.class, args);
	}
}
