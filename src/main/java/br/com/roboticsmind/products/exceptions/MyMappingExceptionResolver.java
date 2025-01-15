package br.com.roboticsmind.products.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(MyMappingExceptionResolver.class);

    public MyMappingExceptionResolver() {
        // Set logging configuration
        setWarnLogCategory(MyMappingExceptionResolver.class.getName());
    }

    @Override
    public String buildLogMessage(Exception e, HttpServletRequest req) {
        String logMessage = "MVC exception: " + e.getLocalizedMessage();
        logger.error(logMessage, e); // Log the exception
        return logMessage;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest req,
                                              HttpServletResponse resp, Object handler, Exception ex) {
        // Log the exception details
        logger.error("Exception occurred while processing request to {}", req.getRequestURL(), ex);

        // Call the super method to get the ModelAndView
        ModelAndView mav = super.doResolveException(req, resp, handler, ex);

        // Add the full URL to the ModelAndView
        if (mav != null) {
            mav.addObject("url", req.getRequestURL());
        }
        return mav;
    }
}
