package com.laioffer.staybooking.controller;

import org.springframework.web.bind.annotation.RestController;

import com.laioffer.staybooking.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;

import com.laioffer.staybooking.model.User;
import com.laioffer.staybooking.model.UserRole;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController // 或者按上个项目的写法， RequestBody 帮助转换成Json格式
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register/guest")
    public void addGuest(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_GUEST); //spring 提供的是Exception Handler 统一处理
    }

    @PostMapping("/register/host")
    public void addHost(@RequestBody User user) {
        registerService.add(user, UserRole.ROLE_HOST);
    }

}