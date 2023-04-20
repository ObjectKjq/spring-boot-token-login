package com.kjq.service;

import com.kjq.pojo.User;

public interface UserService {
    User findByUsername(User user);
    User findUserById(String userId);

    User addUser(User user);
}
