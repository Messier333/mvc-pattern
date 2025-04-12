package com.nhnacademy.springbootmvc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @JsonProperty("id")
    String id;
    @JsonProperty("pwd")
    String password;
    @JsonProperty("name")
    String name;
    @JsonProperty("auth")
    Auth auth;

    private User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public static User create(String id, String password){
        return new User(id, password);
    }

    private static final String MASK = "*****";
    public static User maskedCreate(User user){
        User u = User.create(user.id, MASK);
        u.setName(user.name);
        u.setAuth(user.auth);
        return u;
    }

}
