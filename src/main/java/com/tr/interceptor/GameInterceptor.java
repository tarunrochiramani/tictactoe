package com.tr.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Enumeration;

import com.tr.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//TODO : Validate Token is coming from Slack
@Component
public class GameInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(GameInterceptor.class);
    private static final String SLACK_TOKEN = "x0dU4LW87DX0L3zcBx9BM8ou";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) {
        String method = request.getMethod();
        String token = request.getParameter(Constants.SLACK_REQUEST_PARAM_TOKEN);

        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            logger.info("Param : " + paramName + " = " + request.getParameter(paramName));
        }

        logger.info("RequestURI - " + request.getRequestURI() + " method - " + method + " token - " + token) ;
        return true;
    }
}
