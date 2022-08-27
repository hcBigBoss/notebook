package com.itheima.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public  static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        log.info("拦截到请求: {}",req.getRequestURI());

        String[] urls = new String[]{

                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

//        if (PATH_MATCHER.match("/backend/index.html", req.getRequestURI()) && req.getSession().getAttribute("employee")==null) {
//
//            resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//            return;
//        }

        boolean check = check(urls, req.getRequestURI());
        if(check){
            log.info("本次请求 {} 不需要处理 ",req.getRequestURI());
            filterChain.doFilter(req, resp);
            return;
        }
        if(req.getSession().getAttribute("employee") != null){

            log.info("用户已登录!");
            filterChain.doFilter(req, resp);
            return;

        }


        log.info("用户未登录!");
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


    }

    public boolean check(String[] urls,String requestURI){


        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);

            if(match){
                return true;
            }
        }
        return false;
    }


}
