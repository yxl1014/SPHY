package com.easyarch.using.controller;

import com.easyarch.using.annotation.LogRecord;
import com.easyarch.using.annotation.PassToken;
import com.easyarch.using.entity.Resp;
import com.easyarch.using.entity.User;
import com.easyarch.using.service.UserDo.UserFriendsService;
import com.easyarch.using.service.Users.UserService;
import com.easyarch.using.support.JWTUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFriendsService friendsService;

    private Gson gson = new Gson();

    @PostMapping(value = "adduser")
    @LogRecord(operation = "用户操作", type = "注册")
    @PassToken
    public String addUser(@RequestBody User user, @RequestParam(name = "password") String password) {
        if (user == null) {
            return gson.toJson(new Resp("400", "fail", "用户为空", null, null));
        }
        if (user.getUsername() == null || user.getPassword() == null) {
            return gson.toJson(new Resp("400", "fail", "用户名或密码为空", null, null));
        }
        if (!user.getPassword().equals(password)) {
            return gson.toJson(new Resp("412", "fail", "两次密码不相同", null, null));
        }
        switch (userService.addUser(user)) {
            case 0: {
                return gson.toJson(new Resp("412", "fail", "用户名已存在！", null, null));
            }
            case 1: {
                return gson.toJson(new Resp("200", "success", "注册成功", user.getUser_id(), null));
            }
            default: {
                return gson.toJson(new Resp("500", "fail", "数据库出错", null, null));
            }
        }
    }

    @PostMapping(value = "login")
    @LogRecord(operation = "用户操作", type = "登录")
    @PassToken
    public String login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return gson.toJson(new Resp("400", "fail", "用户名或密码为空", null, null));
        User user;
        if ((user = userService.login(username, password)) != null)
            return gson.toJson(new Resp("200", "success", "注册成功", user.getUser_id(), JWTUtil.sign(user, 60 * 60 * 24 * 7)));
        else
            return gson.toJson(new Resp("400", "fail", "未找到该用户", null, null));
    }

    @PostMapping(value = "find")
    @Cacheable(cacheNames = "user", key = "#username")
    @LogRecord(operation = "用户操作", type = "通过用户名和密码查询用户")
    //public User findByup(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
    public String findByup(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return gson.toJson(new Resp("400", "fail", null, null, null));
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null || user.getName() == null)
            return gson.toJson(new Resp("400", "fail", user, null, null));
        //return user;
        return gson.toJson(new Resp("200", "success", "查找用户名：" + user.getUsername(), null, null));
    }

    @PostMapping(value = "update")
    @LogRecord(operation = "用户操作", type = "修改用户信息")
    public String update(@RequestBody User user, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (user == null) {
            return gson.toJson(new Resp("400", "fail", "修改信息为空", null, null));
        }
        if (username == null || password == null)
            return gson.toJson(new Resp("400", "fail", "用户名或密码为空", null, null));
        if (userService.updateByUsernameAndPassword(username, password, user))
            return gson.toJson(new Resp("200", "success", "修改成功", null, null));
        else
            return gson.toJson(new Resp("400", "fail", "修改失败", null, null));
    }

    @PostMapping(value = "updatepassword")
    @LogRecord(operation = "用户操作", type = "修改密码")
    public String updatePassword(@RequestParam(name = "username") String username, @RequestParam(name = "password") String oldpassword,
                                 @RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2) {
        if (username == null || oldpassword == null || password1 == null || password2 == null)
            return gson.toJson(new Resp("400", "fail", "用户名或密码为空", null, null));
        if (!password1.equals(password2)) {
            return gson.toJson(new Resp("412", "fail", "两次密码不相同", null, null));
        }
        if (userService.updatePassword(username, oldpassword, password1)) {
            return gson.toJson(new Resp("200", "success", "修改成功", null, null));
        } else
            return gson.toJson(new Resp("400", "fail", "修改失败", null, null));
    }

    @PostMapping(value = "deleteUser")
    @LogRecord(operation = "用户操作", type = "注销用户")
    public String deleteUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return gson.toJson(new Resp("400", "fail", "用户名或密码为空", null, null));
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            return gson.toJson(new Resp("400", "fail", "未找到该用户", null, null));
        }
        friendsService.deleteUsers(user.getUser_id());
        if (userService.deleteUser(username, password))
            return gson.toJson(new Resp("200", "success", "删除成功", null, null));
        else
            return gson.toJson(new Resp("500", "fail", "删除失败", null, null));
    }
}
