package com.xc.springbootpractice.Controller;

import com.xc.springbootpractice.Excpetion.UserNotExistException;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UserNotExistException.class)
    public String handlerException(Exception e, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        request.setAttribute("javax.servlet.error.status_code",400);
        map.put("code", "user.NotExist");
        map.put("Message", e.getMessage());

        request.setAttribute("extErr", map);
        return "forward:/error";
    }


}
