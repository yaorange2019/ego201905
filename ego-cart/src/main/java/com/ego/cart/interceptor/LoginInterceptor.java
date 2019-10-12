package com.ego.cart.interceptor;

import com.ego.auth.entity.UserInfo;
import com.ego.auth.utils.JwtUtils;
import com.ego.cart.properties.JwtProperties;
import com.ego.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈〉
 *
 * @author coach tam
 * @email 327395128@qq.com
 * @create 2019/10/12
 * @since 1.0.0
 * 〈坚持灵活 灵活坚持〉
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static UserInfo getUserinfo() {
        return threadLocal.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        //获取cookie
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        try {
            UserInfo userinfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //将userinfo存入threadLocal
            threadLocal.set(userinfo);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
}
