package com.example.demo.DTO;

import com.example.demo.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserResponse {
    private final String id;
    private final String email;
    private final String password;

    public UserResponse (UserEntity userEntity){
        this.id = userEntity.getId();
        this.password = userEntity.getPassword();
        this.email = userEntity.getEmail();
    }
}
