package com.nhnacademy.springbootmvc.service;

import com.nhnacademy.springbootmvc.domain.Auth;
import com.nhnacademy.springbootmvc.domain.User;
import org.springframework.stereotype.Service;

public interface UserService {
    boolean exist(String id);
    boolean match(String id, String password);
    User getUser(String id);
    Auth userAuth(User user);
}
