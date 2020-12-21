package com.easyarch.controller;

import com.easyarch.dao.Userdao;
import com.easyarch.entity.User;
import com.easyarch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "adduser")
    public void addUser(@RequestBody User user){

    }
}
