package com.xc.springbootpractice.config;

import com.xc.springbootpractice.component.LoginHandlerInterceptor;
import com.xc.springbootpractice.component.MyLocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/hello").setViewName("success");
    }


    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer adapter = new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {

                registry.addViewController("/").setViewName("index");
                registry.addViewController("/index.html").setViewName("index");
                registry.addViewController("/listing.html").setViewName("listing");
                registry.addViewController("/form.html").setViewName("form");
                registry.addViewController("/add.html").setViewName("add");
            }

            /*@Override
            public void addInterceptors(InterceptorRegistry registry) {
                //拦截任意路径请求
                registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**")
                        .excludePathPatterns("/login.html","/","/user/login","/css/**","/images/**","/layui/**","/js/**","/img/**","/font/**");
            }*/
        };
        return adapter;
    }


        //在容器中注册这个对象 替代默认的国际化工具
    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocalResolver();
    }
}
