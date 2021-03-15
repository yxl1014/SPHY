package com.easyarch.dao;

import com.easyarch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Userdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addUser(User user){
        String sql="insert into USER (name,username,password,tel,birthday,home,company) values(?,?,?,?,?,?,?)";
        int i=jdbcTemplate.update(sql,new Object[]{user.getName(),user.getUsername(),user.getPassword(),user.getTel(),
        user.getBirthday(),user.getHome(),user.getCompany()});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteByUsernameAndPassword(String username,String password){
        String sql="delete from USER where username=? and password=?";
        int i=jdbcTemplate.update(sql,new Object[]{username,password});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean updatepassword(String username,String password,String newpwd){
        String sql="update USER set password= ? where username=? and password =?";
        int i=jdbcTemplate.update(sql,new Object[]{newpwd,username,password});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public User findbyId(String id){
        String sql="select id,name,username,password,tel,birthday,home,company from user where id=?";
        List<User> list=jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(User.class),id);
        if(list.size()==0)
            return null;
        else
            return list.get(0);
    }

    public User findbyUsernameAndPassword(String username,String password){
        String sql="select id,name,username,password,tel,birthday,home,company from USER where username=? and password=?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class),username,password);
        if(list.size()==0)
            return null;
        else
            return list.get(0);
    }

    public Integer findbyUsername(String username){
        String sql="select count(1) from USER where username=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,username);
    }

    public Integer getAllUser(){
        String sql="select count(1) from USER";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public void deleteAllUser(){
        String sql="delete from USER";
        jdbcTemplate.update(sql);
    }
}
