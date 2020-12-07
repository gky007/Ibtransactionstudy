package com.jamie.server1.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/7 17:29
 * @description
 */
@Configuration
public class PageHelperConfig {
    @Bean
    public PageHelper getPageHelper(){
        PageHelper pageHelper=new PageHelper();
        Properties properties=new Properties();
        properties.setProperty("helperDialect","mysql");
        properties.setProperty("reasonable","true");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("params","count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
