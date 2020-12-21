package com.easyarch.dao;

import com.easyarch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Userdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addUser(User user){
        String sql="insert into first(name,username,password,tel,birthday,home,company) values(?,?,?,?,?,?,?)";
        int i=jdbcTemplate.update(sql,new Object[]{user.getName(),user.getUsername(),user.getPassword(),user.getTel(),
        user.getBirthday(),user.getHome(),user.getCompany()});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteUser(User user){
        String sql="delete from first where username=? and password=?";
        int i=jdbcTemplate.update(sql,new Object[]{user.getUsername(),user.getPassword()});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean updatepassword(User o,User n){
        String sql="update first set password= ? where username=? and password =?";
        int i=jdbcTemplate.update(sql,new Object[]{n.getPassword(),o.getUsername(),o.getPassword()});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }
}
