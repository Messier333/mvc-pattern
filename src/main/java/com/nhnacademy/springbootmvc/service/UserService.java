package com.nhnacademy.springbootmvc.service;

import com.nhnacademy.springbootmvc.domain.User;

public interface UserService {
    boolean exist(String id);
    boolean match(String id, String password);
    User getUser(String id);
}
