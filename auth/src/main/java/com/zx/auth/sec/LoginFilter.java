package com.zx.auth.sec;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.zx.common.common.RedisUtil;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.Resource;
import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 登录过滤器
 *
 * @author hmchen
 * @date 2020年2月7日16:40:30
 */
@Component
public class LoginFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
    @Resource
    RedisUtil redisUtil;

    @Value("${login.excludeUrl}")
    private String excludeUrl;
    @Value("${login.session.timeout}")
    private String loginSessionTimeOut;

    private PathMatcher pathMatcher;


    private String[] excludeUrls;
    private static final String ticket_ = "ticket_";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.pathMatcher = new AntPathMatcher();
        if (StringUtils.isNotEmpty(excludeUrl)) {
            this.excludeUrls = excludeUrl.split(",");
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        //是否需要过滤
        boolean needFilter = isNeedFilter(request.getServletPath());
        if (!needFilter) { //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } else { //需要过滤器
            // session中包含user对象,则是登录状态
            if (session != null && session.getAttribute("userInfo") != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String ticket = request.getHeader("ticket");
                if (!StringUtils.isEmpty(ticket)) {
                    String key = ticket_ + ticket;
                    Object values = redisUtil.get(key);
                    if (values != null) {
                        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse((String) values);
                        if (map.get("userInfo") != null) {
                            redisUtil.expire(key, Long.parseLong(loginSessionTimeOut));
                        }

                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    } else {
                        redisUtil.expire(key, 0);
                    }
                }
                //判断是否是ajax请求
                if (this.isAjax(request)) {
                    response.setHeader("sessionstatus", "timeout");
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
                //判断是否是ajax请求
                redirectLogin(response);
                return;
            }
        }
    }


    @Override
    public void destroy() {

    }

    /**
     * url地址过滤：是否需要过滤
     *
     * @param url
     * @return
     */
    public boolean isNeedFilter(String url) {
        if (excludeUrls == null) {
            return true;
        }

        for (String excludeUrl : this.excludeUrls)
            if (this.pathMatcher.match(excludeUrl, url)) {
                return false;
            }

        return true;
    }

    private boolean isAjax(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    private void redirectLogin(HttpServletResponse response) throws IOException {
        ResponseBean result = new ResponseBean(
                CommonConstants.NOT_LOGIN.getCode(),
                CommonConstants.NOT_LOGIN.getMessage());
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.append(JSONObject.toJSONString(result));
    }
}
