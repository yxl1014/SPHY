package com.easyarch.dao;

import com.easyarch.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class Logdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addLog(Log log){
        String sql="insert into LOG(logop,logtype,userid,createtime,url) values(?,?,?,?,?)";
        jdbcTemplate.update(sql,log.getLogOp(),log.getLogType(),log.getUserId(),log.getCreateTime(),log.getUrl());
    }
}
