package com.easyarch.controller;

import com.easyarch.annotation.LogRecord;
import com.easyarch.annotation.PassToken;
import com.easyarch.entity.Resp;
import com.easyarch.entity.User;
import com.easyarch.service.Users.UserService;
import com.easyarch.support.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "adduser")
    @LogRecord(operation = "用户操作", type = "注册")
    @PassToken
    public Resp addUser(@RequestBody User user, @RequestParam(name = "password") String password) {
        if (user == null) {
            return new Resp("400", "fail", "用户为空", null, null);
        }
        if (user.getUsername() == null || user.getPassword() == null) {
            return new Resp("400", "fail", "用户名或密码为空", null, null);
        }
        if (!user.getPassword().equals(password)) {
            return new Resp("412", "fail", "两次密码不相同", null, null);
        }
        switch (userService.addUser(user)) {
            case 0: {
                return new Resp("412", "fail", "用户名已存在！", null, null);
            }
            case 1: {
                return new Resp("200", "success", "注册成功", user.getUser_id(), null);
            }
            default: {
                return new Resp("500", "fail", "数据库出错", null, null);
            }
        }
    }

    //TODO:跳转界面用Requestmapping
    //@RequestMapping(value = "/login",method = RequestMethod.POST)
    @PostMapping(value = "login")
    @LogRecord(operation = "用户操作", type = "登录")
    @PassToken
    public Resp login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return new Resp("400", "fail", "用户名或密码为空", null, null);
        User user;
        if ((user = userService.login(username, password)) != null)
            return new Resp("200", "success", "注册成功", user.getUser_id(), JWTUtil.sign(user, 60 * 60 * 24 * 7));
        else
            return new Resp("400", "fail", "未找到该用户", null, null);
    }

    @PostMapping(value = "find")
    @Cacheable(cacheNames = "user", key = "#username")
    @LogRecord(operation = "用户操作", type = "通过用户名和密码查询用户")
    //public User findByup(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
    public Resp findByup(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return new Resp("400", "fail", null, null, null);
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null || user.getName() == null)
            return new Resp("400", "fail", user, null, null);
        //return user;
        return new Resp("200", "success", "查找用户名：" + user.getUsername(), null, null);
    }

    @PostMapping(value = "update")
    @LogRecord(operation = "用户操作", type = "修改用户信息")
    public Resp update(@RequestBody User user, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (user == null) {
            return new Resp("400", "fail", "修改信息为空", null, null);
        }
        if (username == null || password == null)
            return new Resp("400", "fail", "用户名或密码为空", null, null);
        if (userService.updateByUsernameAndPassword(username, password, user))
            return new Resp("200", "success", "修改成功", null, null);
        else
            return new Resp("400", "fail", "修改失败", null, null);
    }

    @PostMapping(value = "updatepassword")
    @LogRecord(operation = "用户操作", type = "修改密码")
    public Resp updatePassword(@RequestParam(name = "username") String username, @RequestParam(name = "password") String oldpassword,
                               @RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2) {
        if (username == null || oldpassword == null || password1 == null || password2 == null)
            return new Resp("400", "fail", "用户名或密码为空", null, null);
        if (!password1.equals(password2)) {
            return new Resp("412", "fail", "两次密码不相同", null, null);
        }
        if (userService.updatePassword(username, oldpassword, password1)) {
            return new Resp("200", "success", "修改成功", null, null);
        } else
            return new Resp("400", "fail", "修改失败", null, null);
    }

    @PostMapping(value = "deleteUser")
    @LogRecord(operation = "用户操作", type = "注销用户")
    public Resp deleteUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        if (username == null || password == null)
            return new Resp("400", "fail", "用户名或密码为空", null, null);
        if (userService.deleteUser(username, password))
            return new Resp("200", "success", "删除成功", null, null);
        else
            return new Resp("400", "fail", "删除失败", null, null);
    }
}
