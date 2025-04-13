package com.nhnacademy.springbootmvc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
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

}
