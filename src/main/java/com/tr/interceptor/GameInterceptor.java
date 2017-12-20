package com.tr.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//TODO : Validate Token is coming from Slack
@Component
public class GameInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(GameInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) {
        String method = request.getMethod();
        String token = request.getHeader("token");
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                logger.info("Header: " + request.getHeader(headerNames.nextElement()));
            }
        }

        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            System.out.println("Param : " + paramName + " = " + request.getParameter(paramName));
        }

        logger.info("RequestURI - " + request.getRequestURI() + " method - " + method + " token - " + token) ;
        return true;
    }
}
