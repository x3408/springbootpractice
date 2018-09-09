package com.xc.springbootpractice.component;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

//给容器中添加自己的错误属性
@Component
public class MyErrorAttributes extends DefaultErrorAttributes{

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> customErrAttr = (Map<String, Object>) webRequest.getAttribute("extErr", 0);
        return customErrAttr;
    }
}
