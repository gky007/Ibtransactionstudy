package com.jamie.server1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@MapperScan("com.jamie.server1.dao") //扫描的mapper
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Server1Application {
    public static void main(String[] args) {
        SpringApplication.run(Server1Application.class, args);
    }
}
