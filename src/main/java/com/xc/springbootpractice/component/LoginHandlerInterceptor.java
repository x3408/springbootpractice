package com.xc.springbootpractice.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");

        if (user == null) {
            request.setAttribute("msg","请先登录");
            //返回登录页
            request.getRequestDispatcher("/login.html").forward(request, response);

            return false;
        }

        //已经登录放行
        return true;
    }

}
