package com.example.model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;


import com.example.service.LogService;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
  private final LogService logService;

   @Autowired
    public RequestLoggingInterceptor(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
           
             String requestBody = getRequestBody(wrapper);
            logger.info("Request: {} {} Parameters: {}", request.getMethod(), request.getRequestURI(), requestBody);
            logger.info("Gelen kullanıcı adı: {}",  extractUsername(wrapper) );
            logger.info("Gelen şifre: {}", extractPassword(wrapper));


       }

         return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws IOException{
         byte[] contentAsByteArray = request.getContentAsByteArray();
        if (contentAsByteArray.length == 0) {
           return "";
        }
        return new String(contentAsByteArray, request.getCharacterEncoding());
    }

     private String extractUsername(ContentCachingRequestWrapper request) throws IOException{

       String requestBody = getRequestBody(request);
       if(requestBody.contains("username")) {
           String username = requestBody.substring(requestBody.indexOf("username") + 11);
           username =  username.substring(0, username.indexOf("\""));
           return username;
        
       }
       return "";
   }

   private String extractPassword(ContentCachingRequestWrapper request) throws IOException{

       String requestBody = getRequestBody(request);
        if(requestBody.contains("password")) {
           String password = requestBody.substring(requestBody.indexOf("password") + 11);
           password =  password.substring(0, password.indexOf("\""));
           return password;
        
       }
       return "";
    }
}