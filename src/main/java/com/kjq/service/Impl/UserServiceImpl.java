package com.kjq.service.Impl;

import com.kjq.mapper.UserMapper;
import com.kjq.pojo.User;
import com.kjq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User findByUsername(User user) {
        return userMapper.findByUsername(user.getUsername());
    }

    @Override
    public User findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    @Override
    public User addUser(User user) {
        userMapper.addUser(user);
//        判断有没有添加成功
        if(user.getId() != null){
            return user;
        }else{
            return null;
        }
    }

}
