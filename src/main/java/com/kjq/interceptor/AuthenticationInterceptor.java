package com.kjq.interceptor;

import com.kjq.pojo.User;
import com.kjq.service.UserService;
import com.kjq.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    // 进入Controller之前做认证
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头里获得token
        String token = request.getHeader("token");
        if(token == null){
            response.sendRedirect("/loginPage");
            return false;
//            throw new RuntimeException("无token，请重新登录");
        }
        //验证token是否是本服务器产生的
        if(!TokenUtil.verify(token)){
            response.sendRedirect("/loginPage");
            return false;
//            throw new RuntimeException("token令牌错误，请重新登录");
        }
        String username = TokenUtil.getUsername(token);
        User user = userService.findByUsername(new User(null, username, null));
        //判断user获取成功没
        if(user == null){
            response.sendRedirect("/loginPage");
            return false;
//            throw new RuntimeException("无此用户，请重新登录");
        }
        //把用户名存储到request域中，方便后端模板渲染
        request.setAttribute("username", user.getUsername());
        return true;
    }
}
