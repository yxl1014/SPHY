package com.easyarch.service;

import com.easyarch.dao.Userdao;
import com.easyarch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private Userdao userdao;

    public boolean addUser(User user){
        if(userdao.addUser(user))
            return true;
        else
            return false;
    }
}
