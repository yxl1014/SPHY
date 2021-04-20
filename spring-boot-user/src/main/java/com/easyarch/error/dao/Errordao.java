package com.easyarch.error.dao;

import com.easyarch.error.entity.Erormessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.PrintStream;
import java.io.PrintWriter;

@Repository
public class Errordao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Erormessage message) {
        String sql = "insert into error_logs(etime,userid,context) values(?,?,?)";
        int i = jdbcTemplate.update(sql, message.getEtime(), message.getUserid(), message.getContext());


        //异常栈输出ｓｑｌ　　ｉｎｓｅｒｔ失败

/*        if(i==0){
            new Exception().printStackTrace(new PrintWriter(""));
        }*/
    }
}
