package com.xc.springbootpractice.Aspeck;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InfoAspeck {
    private static final Logger logger = LoggerFactory.getLogger(InfoAspeck.class);

    @Pointcut(value = "execution(public * com.xc.springbootpractice.Service.*.*(..))")
    public void log() {}

    @AfterReturning(pointcut = "log()", returning = "object")
    public void AfterReturning(Object object) {
        logger.info("object={}", object.toString());
    }
}
