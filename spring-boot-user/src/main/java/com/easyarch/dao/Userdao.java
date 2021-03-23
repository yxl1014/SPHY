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
        String sql="insert into users (name,username,password,user_tel,user_email) values(?,?,?,?,?);";
        int i=jdbcTemplate.update(sql,new Object[]{user.getName(),user.getUsername(),user.getPassword()
                ,user.getUser_tel(),user.getUser_email()});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteByUsernameAndPassword(String username,String password){
        String sql="delete from users where username=? and password=?;";
        int i=jdbcTemplate.update(sql,new Object[]{username,password});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public boolean updatepassword(String username,String password,String newpwd){
        String sql="update users set password= ? where username=? and password =?;";
        int i=jdbcTemplate.update(sql,new Object[]{newpwd,username,password});
        if(i!=0){
            return true;
        }else{
            return false;
        }
    }

    public User findbyId(String id){
        String sql="select user_id,name,username,password,user_tel,user_email from users where user_id=?;";
        List<User> list=jdbcTemplate.queryForList(sql,User.class,id);
        if(list.size()==0)
            return null;
        else
            return list.get(0);
    }

    public User findbyUsernameAndPassword(String username,String password){
        String sql="select id,name,username,password,tel,birthday,home,company from users where username=? and password=?;";
        List<User> list = jdbcTemplate.queryForList(sql,User.class,username,password);
        if(list.size()==0)
            return null;
        else
            return list.get(0);
    }

    public User findbyUsername(String username){
        String sql="select id,name,username,password,tel,birthday,home,company from users where username=?;";
        List<User> list = jdbcTemplate.queryForList(sql,User.class,username);
        if(list.size()==0)
            return null;
        else
            return list.get(0);
    }

    public Integer findAllbyUsername(String username){
        String sql="select count(1) from users where username=?;";
        return jdbcTemplate.queryForObject(sql,Integer.class,username);
    }


    public Integer getAllUser(){
        String sql="select count(1) from users";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public void deleteAllUser(){
        String sql="delete from users";
        jdbcTemplate.update(sql);
    }
}
