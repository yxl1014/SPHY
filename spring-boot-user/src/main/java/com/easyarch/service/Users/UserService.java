package com.easyarch.service.Users;

import com.easyarch.dao.Userdao;
import com.easyarch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService implements IUserService {

    @Autowired
    private Userdao userdao;


    @Override
    public int addUser(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int user1 = userdao.findAllbyUsername(user.getUsername());
        if (user1 != 0) {
            return 0;
        }
        if (userdao.addUser(user))
            return 1;
        else
            return -1;
    }

    @Override
    public User login(String username, String password) {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        return userdao.findbyUsernameAndPassword(username, pwd);
    }

    @Override
    public Integer getAllUser() {
        return userdao.getAllUser();
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        return userdao.findbyUsernameAndPassword(username, pwd);
    }

    @Override
    public boolean updateByUsernameAndPassword(String username, String password, User n) {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        User old = userdao.findbyUsernameAndPassword(username, pwd);
        if (old == null || old.getName() == null)
            return false;
        if (n.getName() != null)
            n.setName(old.getName());
        if (n.getUser_email() == null)
            n.setUser_email(old.getUser_email());
        if (n.getUser_tel() == null)
            n.setUser_tel(old.getUser_tel());
        n.setUsername(username);
        n.setPassword(pwd);
        userdao.deleteByUsernameAndPassword(username, pwd);
        return userdao.addUser(n);
    }

    @Override
    public boolean updatePassword(String username, String oldpassword, String newpassword) {
        String oldpwd = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
        String newpwd = DigestUtils.md5DigestAsHex(newpassword.getBytes());
        return userdao.updatepassword(username, oldpwd, newpwd);
    }

    @Override
    public boolean deleteUser(String username, String password) {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        return userdao.deleteByUsernameAndPassword(username, pwd);
    }

    @Override
    public void deleteAllUser() {
        userdao.deleteAllUser();
    }
}
