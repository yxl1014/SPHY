package com.easyarch.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebLoginConfig implements WebMvcConfigurer {

    @Autowired
    public CheckHandle Handle;

    //    @Bean
//    public LoginHandle getLoginHandler(){
//        return new LoginHandle();
//    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {//设置通过CheckHandle来拦截所有的访问路径
        registry.addInterceptor(Handle).addPathPatterns("/**");
    }
//
//    @Bean
//    public CheckHandle checkHandle() {
//        return new CheckHandle();
//    }
}
