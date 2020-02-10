package com.zx.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRedisHttpSession
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan({
        "com.zx.*.sec",
        "com.zx.*.generator",
        "com.zx.*.common",
        "com.zx.*.schedule",
        "com.zx.*.controller",
        "com.zx.*.service"
})
@MapperScan("com.zx.*.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
