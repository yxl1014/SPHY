package com.easyarch.service;

import com.easyarch.entity.User;

public interface IUserService {

    int addUser(User user);

    boolean login(String username,String password);

    Integer getAllUser();

    User findByUsernameAndPassword(String username,String password);

    boolean updateByUsernameAndPassword(String username,String password,User n);

    boolean updatePassword(String username,String oldpassword,String password);

    boolean deleteUser(String username, String password);

    void deleteAllUser();
}
