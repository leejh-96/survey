package com.tys.survey.config;

import com.tys.survey.interceptor.LoginCheckInterceptor;
import com.tys.survey.interceptor.MemberRoleCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("","/","/css/**","/img/**","/join/**","/login","/logout","/loginForm"
                                    ,"/notice/list/**","/notice/detail/**","/survey/**","/findId","/findPassword");

        registry.addInterceptor(new MemberRoleCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("","/","/css/**","/img/**","/join/**","/login","/logout","/loginForm"
                                    ,"/notice/list/**","/notice/detail/**","/survey/**","/findId","/findPassword","/mypage/**");
    }
}
