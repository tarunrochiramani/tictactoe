package com.tr.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tr.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class GameInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(GameInterceptor.class);

    @Autowired private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) {
        String method = request.getMethod();
        String token = request.getParameter(Constants.SLACK_REQUEST_PARAM_TOKEN);

        if (!Constants.SLACK_INTERACTIVE_URI.equals(request.getRequestURI()) &&
                !token.equals(environment.getProperty(Constants.APPLICATION_PROP_SLASH_CMD_TOKEN))) {
            logger.error("Invalid request. Not from Slack. Mismatch in slash command token.");
            return false;
        }

        logger.info("RequestURI - " + request.getRequestURI() + " method - " + method) ;
        return true;
    }
}
