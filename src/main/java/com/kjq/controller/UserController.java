package com.kjq.controller;

import com.alibaba.fastjson.JSONObject;
import com.kjq.pojo.User;
import com.kjq.service.UserService;
import com.kjq.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    // 用户登录
    @PostMapping("/login")
    @ResponseBody
    public Object login(User user){
        JSONObject jsonObject = new JSONObject();
        //通过userService查询用户
        User userForBase = userService.findByUsername(user);
        if(userForBase == null){
            jsonObject.put("message", "登陆失败，用户不存在");
            return jsonObject;
        }else{
            //判段密码是否正确
            if(!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message", "密码错误");
                return jsonObject;
            }else{
                // 使用用户名与密码生成token
                String token = TokenUtil.sign(user);
                jsonObject.put("token",token);
                jsonObject.put("name", user.getUsername());
                //登录成功返回json对象，json中保存的有token，前端来处理
                return jsonObject;
            }
        }
    }

    //用户注册
    @PostMapping("/register")
    @ResponseBody
    public Object register(User user){
        JSONObject jsonObject = new JSONObject();
        //判断用户名和密码是否存在
        if(user.getUsername() == null || user.getPassword() == null){
            jsonObject.put("message","请输入用户名或密码！！");
        }
        //判断用户名是否存在，如果存在就注册失败
        User user1 = userService.findByUsername(user);
        if(user1 != null){
            jsonObject.put("message","用户名已存在！！");
            return jsonObject;
        }
        //添加用户并返回对象
        User getUser = userService.addUser(user);
        if(getUser == null){
            jsonObject.put("message","用户注册失败");
            return jsonObject;
        }
        // 使用用户名与密码生成token
        String token = TokenUtil.sign(getUser);
        jsonObject.put("token",token);
        jsonObject.put("name", getUser.getUsername());
        //登录成功返回json对象，json中保存的有token，前端来处理
        return jsonObject;
    }

    //主页，用户没有登录是没有权限访问主页的
    @GetMapping("/index")
    @ResponseBody
    public Object index(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        if(request.getAttribute("username") != null){
            jsonObject.put("username", request.getAttribute("username"));
        }
        return jsonObject;
    }

    //登录注册主页
    @GetMapping("/loginPage")
    public String login(){
        return "loginRegister";
    }
}
