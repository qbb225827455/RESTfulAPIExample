package com.example.demo.Config;

import com.example.demo.Filter.LogAPIFilter;
import com.example.demo.Filter.LogProcessTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean logAPIFilter() {
        FilterRegistrationBean<LogAPIFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new LogAPIFilter());
        bean.addUrlPatterns("/*");
        bean.setName("LogAPIFilter");
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean logProcessTimeFilter() {
        FilterRegistrationBean<LogProcessTimeFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new LogProcessTimeFilter());
        bean.addUrlPatterns("/*");
        bean.setName("LogProcessTimeFilter");
        bean.setOrder(1);
        return bean;
    }
}



