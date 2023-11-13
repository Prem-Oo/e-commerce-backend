package com.customer.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

//@Component
//public class CorsFilter extends GenericFilterBean {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
//            throws IOException, ServletException {
//    	   HttpServletResponse response = (HttpServletResponse) res;
//           HttpServletRequest request = (HttpServletRequest) req;
//
//           response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
//           response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
//           response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
//           response.setHeader("Access-Control-Max-Age", "3600");
//           response.setHeader("Access-Control-Allow-Credentials", "true"); // Set this to "true" to allow credentials (e.g., cookies, authorization headers)
//
//           // For preflight requests, respond with a 200 OK status
//           if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//               response.setStatus(HttpServletResponse.SC_OK);
//           } else {
//               filterChain.doFilter(req, res);
//           }
//    }
//}
