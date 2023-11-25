package com.atguigu.auth.activiti;

import org.springframework.stereotype.Component;

@Component
public class UserBean {
    public String getUsername(int id) {
        if(id == 1) {
            return "haha";
        }
        if(id == 2) {
            return "hehe";
        }
        return "admin";
    }
}
