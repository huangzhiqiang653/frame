package com.zx.rts;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ComponentScan({
        "com.zx.*.generator",
        "com.zx.*.common",
        "com.zx.*.controller",
        "com.zx.*.service",
        "com.zx.*.scheduler",
        "com.zx.*.config",
})
@MapperScan("com.zx.*.mapper")
public class RtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtsApplication.class, args);
    }

}
