package com.alex.controller.interceptor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "transmitFilter")
public class transmitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            ServletRequest request = new RequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(request, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
