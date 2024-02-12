package com.fbert.TaskManager.dto;

import lombok.Getter;

@Getter
public class UserRegisterDTO {
    private String login;
    private String password;
    private String role;

    public UserRegisterDTO(String login, String password){
        this.login = login;
        this.password = password;
        this.role = "USER";
    }

    public void setRole(String role) {
        this.role = "USER";
    }

}
