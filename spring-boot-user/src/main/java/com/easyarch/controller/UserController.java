package com.easyarch.controller;

import com.easyarch.annotation.LogRecord;
import com.easyarch.entity.User;
import com.easyarch.service.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "adduser")
    @LogRecord(operation = "用户操作" ,type = "注册")
    public String addUser(@RequestBody User user,@RequestParam(name = "password")String password){
        if(user==null){
            return "用户为空！";
        }
        if(user.getUsername()==null||user.getPassword()==null){
            return "用户名或密码不能为空！";
        }
        if(!user.getPassword().equals(password)){
            return "两次密码不相同！";
        }
        switch (userService.addUser(user)){
            case 0:{
                return "用户名已存在！";
            }
            case 1:{
                return "注册成功！";
            }
            default:{
                return "注册失败！";
            }
        }
    }

    //TODO:跳转界面用Requestmapping
    //@RequestMapping(value = "/login",method = RequestMethod.POST)
    @PostMapping(value = "login")
    @LogRecord(operation = "用户操作" ,type = "登录")
    public String login(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
        if(username==null||password==null)
            return "用户名或密码不能为空！";
        if(userService.login(username,password))
            //TODO:若跳转界面，return里面写跳转的html或jsp名
            return "登录成功！";
        else
            return "登录失败！";
    }

    @PostMapping(value = "find")
    @Cacheable(cacheNames = "user", key = "#username")
    @LogRecord(operation = "用户操作",type = "通过用户名和密码查询用户")
    //TODO:可以改成查询出一个用户类的序列化编码
    //public User findByup(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
    public String findByup(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
        if(username==null||password==null)
            return "用户名或密码不能为空！";
        User user=userService.findByUsernameAndPassword(username,password);
        if(user==null)
            return "该用户不存在！";
        if(user.getName()==null)
            return "该用户不存在！";
        //return user;
        return "hello"+user.getName();
    }

    @PostMapping(value = "update")
    @LogRecord(operation = "用户操作",type = "修改用户信息")
    public String update(@RequestBody User user,@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
        if(user==null){
            return "修改信息为空！";
        }
        if(username==null||password==null)
            return "用户名或密码不能为空！";
        if(userService.updateByUsernameAndPassword(username,password,user))
            return "修改成功！";
        else
            return "修改失败！";
    }

    @PostMapping(value = "updatepassword")
    @LogRecord(operation = "用户操作" ,type = "修改密码")
    public String updatePassword(@RequestParam(name = "username")String username,@RequestParam(name = "password")String oldpassword,
                                 @RequestParam(name = "password1")String password1,@RequestParam(name = "password2")String password2){
        if(username==null||oldpassword==null||password1==null||password2==null)
            return "用户名或密码不能为空！";
        if(!password1.equals(password2)){
            return "两次密码不相同！";
        }
        if(userService.updatePassword(username,oldpassword,password1))
            return "修改成功！";
        else
            return "修改失败！";
    }

    @PostMapping(value = "cancellationUser")
    @LogRecord(operation = "用户操作" ,type="注销用户")
    public String deleteUser(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
        if(username==null||password==null)
            return "用户名或密码不能为空！";
        if(userService.deleteUser(username,password))
            return "删除成功！";
        else
            return "删除失败！";
    }
}
