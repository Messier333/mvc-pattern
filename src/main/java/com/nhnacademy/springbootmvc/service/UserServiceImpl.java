package com.nhnacademy.springbootmvc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public UserServiceImpl(){
        try(Reader reader = new FileReader("users.json")){
            ObjectMapper objectMapper = new ObjectMapper();
            User[] user = objectMapper.readValue(reader, User[].class);
            for(User user1 : user){
                userMap.put(user1.getId(), user1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean exist(String id) {
        return !userMap.containsKey(id);
    }

    @Override
    public boolean match(String id, String password) {
        if (exist(id)) {
            throw new UserNotFoundException();
        }
        return Optional.ofNullable(userMap.get(id))
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public User getUser(String id) {
        if (exist(id)) {
            throw new UserNotFoundException();
        }
        log.info("serviceGetUser:{}", userMap.get(id).getId());
        return userMap.get(id);
    }


}
