package com.ego.gateway.filter;

import com.ego.auth.utils.JwtUtils;
import com.ego.common.utils.CookieUtils;
import com.ego.gateway.properties.FilterProperties;
import com.ego.gateway.properties.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/11
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Component
@EnableConfigurationProperties({FilterProperties.class, JwtProperties.class})
public class LoginFilter extends ZuulFilter {


    @Autowired
    private FilterProperties filterProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //根据当前请求路径，判断是否是白名单，如果是白名单中的uri，直接放行
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //api/search/page
        String requestURI = request.getRequestURI();
        return !filterProperties.getAllowPaths().stream().anyMatch(path -> requestURI.startsWith(path));
    }

    @Override
    public Object run() throws ZuulException {
        //鉴权,通过公钥去解析token
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();


        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        try {
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            //返回401
            requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            //直接返回，不要放行
            requestContext.setSendZuulResponse(false);
        }
        return null;
    }
}
