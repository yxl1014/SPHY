package com.easyarch.service.Users;

import com.easyarch.dao.Userdao;
import com.easyarch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService implements IUserService{

    @Autowired
    private Userdao userdao;

    @Override
    public int addUser(User user){
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int user1=userdao.findbyUsername(user.getUsername());
        if(user1!=0){
            return 0;
        }
        if(userdao.addUser(user))
            return 1;
        else
            return -1;
    }

    //TODO:这个地方可以改成String之后返回token，用来验证登录
    @Override
    public boolean login(String username,String password){
        String pwd=DigestUtils.md5DigestAsHex(password.getBytes());
        User user=userdao.findbyUsernameAndPassword(username,pwd);
        if(user!=null)
            return true;
        else
            return false;
    }

    @Override
    public Integer getAllUser(){
        return userdao.getAllUser();
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String pwd=DigestUtils.md5DigestAsHex(password.getBytes());
        User user=userdao.findbyUsernameAndPassword(username,pwd);
        if(user!=null)
            return user;
        else
            return null;
    }

    @Override
    public boolean updateByUsernameAndPassword(String username, String password, User n) {
        String pwd=DigestUtils.md5DigestAsHex(password.getBytes());
        User old=userdao.findbyUsernameAndPassword(username,pwd);
        if(old==null||old.getName()==null)
            return false;
        if(n.getName()==null)
            n.setName(old.getName());
        if(n.getTel()==null)
            n.setTel(old.getTel());
        if(n.getBirthday()==null)
            n.setBirthday(old.getBirthday());
        if(n.getHome()==null)
            n.setHome(old.getHome());
        if(n.getCompany()==null)
            n.setCompany(old.getCompany());
        n.setUsername(username);
        n.setPassword(pwd);
        userdao.deleteByUsernameAndPassword(username,pwd);
        if(userdao.addUser(n))
            return true;
        else
            return false;
    }

    @Override
    public boolean updatePassword(String username, String oldpassword,String newpassword) {
        String oldpwd=DigestUtils.md5DigestAsHex(oldpassword.getBytes());
        String newpwd=DigestUtils.md5DigestAsHex(newpassword.getBytes());
        if(userdao.updatepassword(username,oldpwd,newpwd))
            return true;
        else
            return false;
    }

    @Override
    public boolean deleteUser(String username, String password) {
        String pwd=DigestUtils.md5DigestAsHex(password.getBytes());
        if(userdao.deleteByUsernameAndPassword(username,pwd))
            return true;
        else
            return false;
    }

    @Override
    public void deleteAllUser() {
        userdao.deleteAllUser();
    }
}
