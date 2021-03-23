package com.easyarch.filter;

import com.easyarch.filter.CheckHandle.CheckChatHandle;
import com.easyarch.filter.CheckHandle.CheckMeetingHandle;
import com.easyarch.filter.CheckHandle.CheckUserHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebLoginConfig implements WebMvcConfigurer {

    @Autowired
    private CheckUserHandle userHandle;

    @Autowired
    private CheckMeetingHandle meetingHandle;

    @Autowired
    private CheckChatHandle chatHandle;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {//设置通过CheckHandle来拦截所有的访问路径
        registry.addInterceptor(userHandle).addPathPatterns("/user/**");
        registry.addInterceptor(meetingHandle).addPathPatterns("/meeting/**");
        registry.addInterceptor(chatHandle).addPathPatterns("/chat/**");
    }
}
